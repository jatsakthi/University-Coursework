from search import GoogleSearch, SearchError
import click

# Parameters to call with the script
#@click.command()
#@click.option('--food_name', required=True, help='Name of the Food whose ingredients reqd')
def find(food_name):
	try:
	  print "Got" + str(food_name)
	  gs = GoogleSearch(food_name)
	  gs.results_per_page = 10
	  results = gs.get_results()
	  lines = []
	  for res in results:
	    lines.append(res.encode("utf8"))
	  return lines
	except SearchError, e:
	  print "Search failed: %s" % e

if __name__ == '__main__':
    find()

