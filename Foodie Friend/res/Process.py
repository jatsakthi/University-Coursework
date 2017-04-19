import base64
import os
import re
import sys
import tempfile
import logging
from googleapiclient.discovery import build
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


class Process(object):
    def __init__(self, dropzone_bucket, filename, filetype, project_id):
        self.project_id = project_id
        self.dropzone_bucket = dropzone_bucket
        self.filename = filename
        self.filetype = filetype
        self.vision_client = create_api_client('vision', 'v1')
    def img_to_text(self):
        print "Starting"



        vision_body ={
              "features": [{
                  "type": "TEXT_DETECTION",
                  "maxResults": 10
                }],
              "image": {
                "source": {
                  "gcsImageUri": "gs://{0}/{1}".format(self.dropzone_bucket, self.filename)
                }
              }
        }
        print "Going for"
        print vision_body
        try:
            vision_request = self.vision_client.images().annotate(body={'requests': vision_body})
            vision_response = vision_request.execute()
            print "done"
            print vision_response
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
            print text_response
            return text_response
            #chosen = vision_response['results'][0]['alternatives'][0]
            #self.write_to_bq(chosen['transcript'], chosen['confidence'])
        except Exception, e:
            print "Problem with file {0} with {1}".format(self.filename, str(e))
            Logger.log_writer("Problem with file {0} with {1}".format(self.filename, str(e)))
            pass



