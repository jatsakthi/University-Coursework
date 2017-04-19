from search import GoogleSearch, SearchError
import click

# Parameters to call with the script
@click.command()
@click.option('--food_name', required=True, help='Name of the Food whose ingredients reqd')
def main(food_name):
	try:
	  gs = GoogleSearch(food_name)
	  gs.results_per_page = 10
	  results = gs.get_results()
	  for res in results:
	    print res.encode("utf8")
	except SearchError, e:

	  print "Search failed: %s" % e

if __name__ == '__main__':
    main()


