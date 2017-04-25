import base64
import os
import re
import sys
import tempfile
import logging
import json

from googleapiclient.discovery import build
import googleapiclient.http as e
from googleapiclient import http, errors
from oauth2client.client import GoogleCredentials
from googleapiclient import discovery
from logger import Logger


DISCOVERY_URL = 'https://{api}.googleapis.com/$discovery/rest?version={apiVersion}'
TMP_SAVE_IMG = '/tmp/output.jpeg'


def create_api_client(which_api, version):
    """Returns a Cloud Logging service client for calling the API."""
    credentials = GoogleCredentials.get_application_default()
    return build(which_api, version, credentials=credentials, discoveryServiceUrl=DISCOVERY_URL)

def create_api_client_withKey(which_api, version):
    """Returns a Cloud Logging service client for calling the API."""
    credentials = GoogleCredentials.get_application_default()
    return build(which_api, version, developerKey="AIzaSyCZGWBJttrKhTjcy6tRjSaJA52zctUb1e4")

def create_gcs_client():
    """Returns a Cloud PubSub service client for calling the API."""
    credentials = GoogleCredentials.get_application_default()
    return build('storage', 'v1', credentials=credentials)


class Process(object):
    def __init__(self, bucket, filename, filetype, project_id):
        self.project_id = project_id
        self.bucket = bucket
        self.filename = filename
        self.filetype = filetype
        self.vision_client = create_api_client('vision', 'v1')
	self.gcs_client = create_gcs_client()
	self.cse_client = create_api_client_withKey('customsearch','v1')
    def getFirstImage(self,query):
	res = self.cse_client.cse().list(
      q=str(query),
      cx='017576662512468239146:omuauf_lfve',
	num=1,
	searchType="image",
    ).execute()
	first_image_link = res['items'][0]['link']
	print first_image_link
	
    def upload_object(self,content):
	body = {
        'name': "output/"+self.filename+".txt",
    	}
	
	stream = e.BytesIO()
	for line in content:
		stream.write(line+'\n')
		
	'''
	if readers or owners:
        	body['acl'] = []

    	for r in readers:
		body['acl'].append({
		    'entity': 'user-%s' % r,
		    'role': 'READER',
		    'email': r
		})
    	for o in owners:
		body['acl'].append({
		    'entity': 'user-%s' % o,
		    'role': 'OWNER',
		    'email': o
		})
	'''
	req = self.gcs_client.objects().insert(bucket=self.bucket,body=body,media_body=e.MediaIoBaseUpload(stream, 'text/plain'))
	resp = req.execute()
	return resp

    def img_to_text(self):
        print "Starting"

        vision_body ={
              "features": [{
                  "type": "TEXT_DETECTION",
                  "maxResults": 10
                }],
              "image": {
                "source": {
                  "gcsImageUri": "gs://{0}/{1}".format(self.bucket, self.filename)
                }
              }
        }
        #Logger.log_writer("{0} process stops".format(filename))vision_body
	Logger.log_writer("Request is: {0}".format(vision_body))
        try:
            vision_request = self.vision_client.images().annotate(body={'requests': vision_body})
            vision_response = vision_request.execute()
            print(json.dumps(vision_response,indent=2))
	    Logger.log_writer("Response is: {0}".format(vision_response))
            if 'responses' not in vision_response:
                return {}
            text_response = {}
            for response in vision_response['responses']:
                if 'error' in response:
                        print("API Error for %s: %s" % (
                            self.filename,
                            response['error']['message']
                            if 'message' in response['error']
                            else ''))
                        continue
                if 'textAnnotations' in response:
                        text_response[self.filename] = response['textAnnotations'][0]['description'].split("\n")
                else:
                        text_response[self.filename] = []           
            Logger.log_writer("Returning from Process: {0}".format(text_response))
            return text_response
        except Exception, e:
            print "Problem with file {0} with {1}".format(self.filename, str(e))
            Logger.log_writer("Problem with file {0} with {1}".format(self.filename, str(e)))
            pass


