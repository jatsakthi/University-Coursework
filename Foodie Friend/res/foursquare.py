import json
import requests
import sys
import urllib
import base64
import xml.etree.cElementTree as ET

try:
    # For Python 3.0 and later
    from urllib.error import HTTPError
    from urllib.parse import quote
    from urllib.parse import urlencode
except ImportError:
    # Fall back to Python 2's urllib2 and urllib
    from urllib2 import HTTPError
    from urllib import quote
    from urllib import urlencode


class FourSquare:
	def __init__(self,lat,lon):
		print ("In FUNCTION: {}".format("FourSquare.__init__"))
		self.id = "DTYSB1VBNV55PWB0GWCSNDVB3E4J1GUV3OGF1D5LO4I3SG4Q"
		self.client = "CSOGOKCN2YOAM4BPKORFMDDGB5VONDK1OVJZRIMT55T45IYM"
		self.lat = str(lat)
		self.lon = str(lon)
		self.URL = "https://api.foursquare.com/v2/venues/explore"
		
	def req(self):
		print ("In FUNCTION: {}".format("FourSquare.req"))
		data = urlencode({
		'client_id': self.id,
		'client_secret': self.client,
		'll': self.lat+','+self.lon,
		'section':'food',
		'venuePhotos':'1',
		'v':'20170428',
		'limit':'5',
		'openNow':'1'
    		})
		
		headers = {
        	'content-type': 'application/x-www-form-urlencoded',
    		}
	
		response = requests.get(self.URL, params=data, headers=headers)
		print ("Foursquare Response URL:{}".format(response.url))
		data = response.json()["response"]
		items = data["groups"][0]["items"]

		restaurants = ET.Element("restaurants")
		for item in items:
			if item.has_key("tips"):
				restaurant = ET.SubElement(restaurants, "restaurant")
				#print(json.dumps(item,indent=2))
				name = item["venue"]["name"].encode('utf-8')

				address = ",".join(item["venue"]["location"]["formattedAddress"])
				address = address.encode('utf-8')

				Type = item["venue"]["categories"][0]["name"].encode('utf-8')

				pic = item["venue"]["photos"]["groups"][0]["items"][0]
				link = pic["prefix"].encode('utf-8')+'original'+pic["suffix"].encode('utf-8')

				comment = item["tips"][0]["text"].encode('utf-8')
				location = item["venue"]["location"]
				latitude = location["lat"]
				longitude = location["lng"]

				ET.SubElement(restaurant, "name", name="name").text = name
				ET.SubElement(restaurant, "address", name="address").text = address
				ET.SubElement(restaurant, "type", name="type").text = Type
				ET.SubElement(restaurant, "link", name="link").text = link
				ET.SubElement(restaurant, "comment", name="comment").text = comment

				#coordinates = ET.SubElement(restaurant, "coordinates")
				ET.SubElement(restaurant, "latitude", name="latitude").text = str(latitude)
				ET.SubElement(restaurant, "longitude", name="longitude").text = str(longitude)
		
				#print "--------------------------------{}------------".format(name)
		#print(json.dumps(data,indent=2))
		tree = ET.ElementTree(restaurants)
		#tree.write("filename.xml")
		return tree
	

if __name__=='__main__':
	c = 33.30
	d = -111.84
	e = FourSquare(c,d)
	f = e.req()
	
