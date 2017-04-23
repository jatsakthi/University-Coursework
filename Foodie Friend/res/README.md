Resource files

Verison 2:
1. User uploads cropped Image to source bucket to which watcher is connected
2. User gets back the details of the food at output folder in the same bucket.

3. No result handled
4. No extracted text case handled
5. Encoding errors handled
Details are gathered by wikipedia.

Commands:
---------
1. gcloud app deploy 01_watcher/app.yaml --bookshelf-164400
2. gsutil notification watchbucket https://bookshelf-164400.appspot.com/media-processing-hook gs://audio-mediap-dropzone
3. gsutil -m cp ~/Pictures/test.png gs://audio-mediap-dropzone
4. gsutil notification stopchannel cb8c5cfe-36f2-4295-8ac4-e009c1b3b91d 3e1NjKnzEL_8hG4sMeZkNTh8LJE
5. python worker.py --subscription=mediap-sub
