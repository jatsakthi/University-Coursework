import base64
import os
import re
import sys
import tempfile
import logging
import json
import urllib
import cStringIO

from googleapiclient.discovery import build
import googleapiclient.http as e
from googleapiclient import http, errors
from oauth2client.client import GoogleCredentials
from googleapiclient import discovery
from logger import Logger
from foursquare import FourSquare


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
		print "FUNCTION: constructor"
		Logger.log_writer("FUNCTION: constructor")
		print ("bucket:{0}".format(str(bucket)))
		print ("filename:{0}".format(str(filename)))
		print ("filetype:{0}".format(str(filetype)))
		print ("project_id:{0}".format(str(project_id)))

		self.project_id = project_id
		self.bucket = bucket
		self.filename = filename
		self.filetype = filetype
		self.vision_client = create_api_client('vision', 'v1')
		self.gcs_client = create_gcs_client()
		self.cse_client = create_api_client_withKey('customsearch','v1')
		self.uploadFileName = self.filename.split("/")[1]
		self.uploadFolderName = self.filename.split("/")[0].split("_")[0]
		self.uploadFileName = self.uploadFileName.split(".")[0]
	
	def getFirstImage(self,query):
		print "FUNCTION: getFirstImage"
		Logger.log_writer("FUNCTION: getFirstImage")
		#resultFound = False
		#types = ('jpg')
		#current = 0
		#while current<=0 and resultFound == False:
		res = self.cse_client.cse().list(
		q=str(query),
		cx='002657803801302330803:gatc1h4ugpi',
		num=1,
		searchType="image",
		imgSize="medium",
		imgType="photo",
		fileType="jpg",
	    	).execute()
		'''
			if len(res['items'])==1:
				resultFound = True
			else:
				current += 1
		'''
		first_image_link = res['items'][0]['link']
		print first_image_link
		Logger.log_writer("Image Link:{}".format(first_image_link))
		print self.upload_image(first_image_link)
	
	def upload_image(self,link):
		print "FUNCTION: upload_image"
		Logger.log_writer("FUNCTION: upload_image")
		parts = link.split(".")
		ext = parts[len(parts)-1]
		body = {
		'name': self.uploadFolderName+"_output"+"/"+self.uploadFileName+"."+str(ext),
		}

		stream = cStringIO.StringIO(urllib.urlopen(link).read())
		req = self.gcs_client.objects().insert(bucket=self.bucket,body=body,media_body=e.MediaIoBaseUpload(stream, "image/jpeg"))
		resp = req.execute()
		#Logger.log_writer("Response:{}".format(resp))
		return resp
	def get_object(self):
		    print "FUNCTION: get_object"
		    Logger.log_writer("FUNCTION: get_object")
		    req = self.gcs_client.objects().get_media(bucket=self.bucket, object=self.filename)
		    out_file = e.BytesIO()
		    downloader = e.MediaIoBaseDownload(out_file, req)

		    done = False
		    while done is False:
			status, done = downloader.next_chunk()
			print("Download {}%.".format(int(status.progress() * 100)))
		    coordinates = out_file.getvalue().split(",")[:2]
		    out_file.close()
		    return coordinates
	def upload_local_image(self,filename):
		print "FUNCTION: Process.upload_local_image"
		Logger.log_writer("FUNCTION: Process.upload_local_image")
		
		body = {
			'name': self.uploadFolderName+"_output"+"/"+self.uploadFileName+".jpg",
		    }
		
		with open(filename, 'rb') as f:
			req = self.gcs_client.objects().insert(
			    bucket=self.bucket, body=body,media_body=e.MediaIoBaseUpload(
				f, "image/jpeg"))
			resp = req.execute()
		return resp
	
	def find_suggestions(self,latitude,longitude):
		print "FUNCTION: Process.find_suggestions"
		Logger.log_writer("FUNCTION: Process.find_suggestions")
		foursquareAPI = FourSquare(latitude,longitude)
		output = foursquareAPI.req()
		print output
		print self.upload_XMLobject(output)

	def upload_XMLobject(self,content):
		print "FUNCTION: upload_XMLobject"
		Logger.log_writer("FUNCTION: upload_XMLobject")
		body = {
        'name': self.uploadFolderName+"_output"+"/"+self.uploadFileName+".xml",
    	}
		stream = e.BytesIO()
		content.write(stream)
		req = self.gcs_client.objects().insert(bucket=self.bucket,body=body,media_body=e.MediaIoBaseUpload(stream, 'text/xml'))
		resp = req.execute()
		return resp
		
	def upload_object(self,content):
		print "FUNCTION: upload_object"
		Logger.log_writer("FUNCTION: upload_object")
		body = {
		'name': self.uploadFolderName+"_output"+"/"+self.uploadFileName+".txt",
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
		print "FUNCTION: img_to_text"
		Logger.log_writer("FUNCTION: img_to_text")

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
		    #print(json.dumps(vision_response,indent=2))
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


