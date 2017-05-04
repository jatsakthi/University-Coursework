#!/usr/bin/python
# encoding: utf-8
#
# Peteris Krumins (peter@catonmat.net)
# http://www.catonmat.net  --  good coders code, great reuse
#
# http://www.catonmat.net/blog/python-library-for-google-search/
#
# Code is licensed under MIT license.
#

import re
import urllib
from BeautifulSoup import BeautifulSoup
from browser import Browser, BrowserError

class SearchError(Exception):
    """
    Base class for Google Search exceptions.
    """
    pass

class ParseError(SearchError):
    """
    Parse error in Google results.
    self.msg attribute contains explanation why parsing failed
    self.tag attribute contains BeautifulSoup object with the most relevant tag that failed to parse
    Thrown only in debug mode
    """
     
    def __init__(self, msg, tag):
        self.msg = msg
        self.tag = tag

    def __str__(self):
        return self.msg

    def html(self):
        return self.tag.prettify()

class SearchResult:
    def __init__(self, title, url, desc):
        self.title = title
        self.url = url
        self.desc = desc

    def __str__(self):
        return 'Google Search Result: "%s"' % self.title


class GoogleSearch(object):
    SEARCH_URL_0 = "http://www.google.%(tld)s/search?hl=%(lang)s&q=%(query)s&btnG=Google+Search"
    NEXT_PAGE_0 = "http://www.google.%(tld)s/search?hl=%(lang)s&q=%(query)s&start=%(start)d"
    SEARCH_URL_1 = "http://www.google.%(tld)s/search?hl=%(lang)s&q=%(query)s&num=%(num)d&btnG=Google+Search"
    NEXT_PAGE_1 = "http://www.google.%(tld)s/search?hl=%(lang)s&q=%(query)s&num=%(num)d&start=%(start)d"

    def __init__(self, query, random_agent=False, debug=False, lang="en", tld="com", re_search_strings=None):
        self.query = query
        self.debug = debug
        self.browser = Browser(debug=debug)
        self.results_info = None
        self.eor = False # end of results
        self._page = 0
        self._first_indexed_in_previous = None
        self._filetype = None
        self._last_search_url = None
        self._results_per_page = 10
        self._last_from = 0
        self._lang = lang
        self._tld = tld
        
        if re_search_strings:
            self._re_search_strings = re_search_strings
        elif lang == "de":
            self._re_search_strings = ("Ergebnisse", "von", u"ungefähr")
        elif lang == "es":
            self._re_search_strings = ("Resultados", "de", "aproximadamente")
        # add more localised versions here
        else:
            self._re_search_strings = ("Results", "of", "about")

        if random_agent:
            self.browser.set_random_user_agent()

    @property
    def num_results(self):
        if not self.results_info:
            page = self._get_results_page()
            self.results_info = self._extract_info(page)
            if self.results_info['total'] == 0:
                self.eor = True
        return self.results_info['total']

    @property
    def last_search_url(self):
        return self._last_search_url

    def _get_page(self):
        return self._page

    def _set_page(self, page):
        self._page = page

    page = property(_get_page, _set_page)

    def _get_first_indexed_in_previous(self):
        return self._first_indexed_in_previous

    def _set_first_indexed_in_previous(self, interval):
        if interval == "day":
            self._first_indexed_in_previous = 'd'
        elif interval == "week":
            self._first_indexed_in_previous = 'w'
        elif interval == "month":
            self._first_indexed_in_previous = 'm'
        elif interval == "year":
            self._first_indexed_in_previous = 'y'
        else:
            # a floating point value is a number of months
            try:
                num = float(interval)
            except ValueError:
                raise SearchError, "Wrong parameter to first_indexed_in_previous: %s" % (str(interval))
            self._first_indexed_in_previous = 'm' + str(interval)
    
    first_indexed_in_previous = property(_get_first_indexed_in_previous, _set_first_indexed_in_previous, doc="possible values: day, week, month, year, or a float value of months")
    
    def _get_filetype(self):
        return self._filetype

    def _set_filetype(self, filetype):
        self._filetype = filetype
    
    filetype = property(_get_filetype, _set_filetype, doc="file extension to search for")
    
    def _get_results_per_page(self):
        return self._results_per_page

    def _set_results_par_page(self, rpp):
        self._results_per_page = rpp

    results_per_page = property(_get_results_per_page, _set_results_par_page)

    def get_results(self):
        """ Gets a page of results """
        if self.eor:
		print "yes"
            	return []
	else:
		print "no"
        MAX_VALUE = 1000000
        page = self._get_results_page()
        #search_info = self._extract_info(page)
        results = self._extract_results(page)
        search_info = {'from': self.results_per_page*self._page,
                       'to': self.results_per_page*self._page + len(results),
                       'total': MAX_VALUE}
        if not self.results_info:
            self.results_info = search_info
            if self.num_results == 0:
                self.eor = True
                return []
        if not results:
            self.eor = True
            return []
        if self._page > 0 and search_info['from'] == self._last_from:
            self.eor = True
            return []
        if search_info['to'] == search_info['total']:
            self.eor = True
        self._page += 1
        self._last_from = search_info['from']
        return results

    def _maybe_raise(self, cls, *arg):
        if self.debug:
            raise cls(*arg)

    def _get_results_page(self):
        if self._page == 0:
            if self._results_per_page == 10:
                url = GoogleSearch.SEARCH_URL_0
            else:
                url = GoogleSearch.SEARCH_URL_1
        else:
            if self._results_per_page == 10:
                url = GoogleSearch.NEXT_PAGE_0
            else:
                url = GoogleSearch.NEXT_PAGE_1

        safe_url = [url % { 'query': urllib.quote_plus(self.query),
                           'start': self._page * self._results_per_page,
                           'num': self._results_per_page,
                           'tld' : self._tld,
                           'lang' : self._lang }]
        
        # possibly extend url with optional properties
        if self._first_indexed_in_previous:
            safe_url.extend(["&as_qdr=", self._first_indexed_in_previous])
        if self._filetype:
            safe_url.extend(["&as_filetype=", self._filetype])
        
        safe_url = "".join(safe_url)
	print "USING" + safe_url
        self._last_search_url = safe_url
        
        try:
            page = self.browser.get_page(safe_url)
        except BrowserError, e:
            raise SearchError, "Failed getting %s: %s" % (e.url, e.error)

        return BeautifulSoup(page)

    def _extract_results(self, soup):
	
	sideBox = soup.findAll('td',{"id" : 'rhs_block'})
	#print sideBox
	for side in sideBox:
		ele = side.findAll('div',{"class":'_o0d'})
		ret_res = []
		#print "#########################################"
		for line in ele:
			#print "########---------->"
			#print line
			texts = line.findAll(text=True)
			#print "T:"+ str(list(texts))
			if len(texts)>0:
				if len(line.findAll('div',{"class":'_B5d'}))>0:
					#print "first found"
					# TITLE LINE
					if len(texts)>1:
						texts[0] += "("+texts[1]+")"
					ret_res.append("Title: \t " + texts[0])
				elif len(line.findAll('div',{"class":'_tXc'}))>0:
					# INTRO LINE
					ret_res.append("Intro: \t " + texts[0])
					#ret_res.append("From: \t " + texts[1])
				elif texts[0] == 'Nutrition Facts': 
					break
				else:
					ret_res.append(texts[0] + ''.join(texts[1:]))
		#print "#########################################"
	'''
        results = soup.findAll('li', {'class': 'g'})
        ret_res = []
        for result in results:
            eres = self._extract_result(result)
            if eres:
                ret_res.append(eres)
        return ret_res
	'''
	#print ret_res
	return ret_res
