#!/usr/bin/python
# Copyright 2013 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#



"""Simple server to demonstrate token verification."""

__author__ = 'Ashana Tayal'

import json
import random
import string
import MySQLdb
import os
import datetime


from flask import Flask
from flask import make_response
from flask import render_template
from flask import request

import httplib2
import oauth2client.client 
from oauth2client.crypt import AppIdentityError
from oauth2client.client import verify_id_token

APPLICATION_NAME = 'Google+ Python Token Verification'


app = Flask(__name__)
app.secret_key = ''.join(random.choice(string.ascii_uppercase + string.digits)
                         for x in range(32))


# Update client_secrets.json with your Google API project information.
# Do not change this assignment.
CLIENT_ID = json.loads(
    open('client_secrets.json', 'r').read())['web']['client_id']




@app.route('/', methods=['GET'])
def index():
  """Render index.html."""
  # Set the Client ID and Application Name in the HTML while serving it.
  response = make_response(
      render_template('index.html',
                      CLIENT_ID=CLIENT_ID,
                      APPLICATION_NAME=APPLICATION_NAME))
  response.headers['Content-Type'] = 'text/html'
  return response

############# TOKEN AUTHENTICATION ###################
@app.route('/auth', methods=['POST'])
def verify():
  """Verify an ID Token or an Access Token."""

  id_token = request.args.get('id_token', None)
  access_token = request.args.get('access_token', None)

  print id_token

  token_status = {}

  id_status = {}
  
  #message = ""
  


  if id_token is not None:
    # Check that the ID Token is valid.
    try:
      # Client library can verify the ID token.
      jwt = verify_id_token(id_token, CLIENT_ID)
      id_status['valid'] = True
      id_status['gplus_id'] = jwt['sub']
      id_status['message'] = 'ID Token is valid.'

      # email_id = jwt['email']
      # user_name = jwt['name']
      # picture_url = jwt['picture']

      # print "email_id",email_id
      # print "user_name",user_name
      # print "picture_url",picture_url

  
      # data['email_id'] = email_id
      # data['name'] = user_name
      # data['photo_url'] = picture_url

    except AppIdentityError:
      id_status['valid'] = False
      id_status['gplus_id'] = None
      id_status['message'] = 'Invalid ID Token.'
    token_status['id_token_status'] = id_status



  response = make_response(json.dumps(token_status, 200))
  response.headers['Content-Type'] = 'application/json'
  return response
 

@app.route('/saveData', methods=['POST'])
def adddiarydetailstodatabase():

 
  #get json data from request
  #datareceived_json = json.loads(request.body)
  app.logger.debug("JSON received...")
  app.logger.debug(request.json)
    
  if request.json:
      datareceived_json = request.json # will be 
      user_id = datareceived_json['user_id']
      print user_id
      diary_title = datareceived_json['diary']['title']
      print diary_title
      diary_date = datareceived_json['diary']['date']
      print diary_date
      diary_content = datareceived_json['diary']['text']
      print diary_content
      
      print "Thanks. Your user id is %s" % datareceived_json.get("user_id")

  else:
      print "no json received"


  # When running on Google App Engine, use the special unix socket
        # to connect to Cloud SQL.
  if os.getenv('SERVER_SOFTWARE', '').startswith('Google App Engine/'):
    db = MySQLdb.connect(
        unix_socket='/cloudsql/{}:{}'.format(
          'digitaldiary-76dfb',
          'diary'),
          user='root',passwd = 'root',db='diarydb')
        # When running locally, you can either connect to a local running
        # MySQL instance, or connect to your Cloud SQL instance over TCP.
  else:
    db = MySQLdb.connect(host='localhost', user='root', passwd = 'root', db = 'diarydb')

  cursor = db.cursor()

  try:
    cursor.execute("insert into diary(userID,diaryTITLE,diaryDATE,diaryCONTENT) values(%s,%s,%s,%s);",(user_id,diary_title,diary_date,diary_content))
    db.commit()
  except:
    db.rollback() 

  
  db.close()

  response = make_response("inserted")
  response.headers['Content-Type'] = 'application/text'
  return response 


@app.route('/retrieveData/<USER_ID>', methods=['GET'])
def getdiarydetailstodatabase(USER_ID):

  print 'UserID %s' % USER_ID



  if os.getenv('SERVER_SOFTWARE', '').startswith('Google App Engine/'):
    db = MySQLdb.connect(
        unix_socket='/cloudsql/{}:{}'.format(
          'digitaldiary-76dfb',
          'diary'),
          user='root',passwd = 'root',db='diarydb')
        # When running locally, you can either connect to a local running
        # MySQL instance, or connect to your Cloud SQL instance over TCP.
  else:
    db = MySQLdb.connect(host='localhost', user='root', passwd = 'root', db = 'diarydb')

  cursor = db.cursor()
  cursor.execute("""select * from diary where userID = {};""".format(USER_ID))


  try:
   
    results = cursor.fetchall()
    count = 0
    diary = []
    for row in results:
      count = count + 1
      diary.append({'title' : row[2], 'date' : row[3], 'text' : row[4]})
      json.dumps(diary)
    print diary
    print count

    obj = {'userid' : USER_ID , 'diaries' : diary}
    json.dumps(obj)
  except:
    print "Error: unable to fecth data"

  
  db.close()

  response = make_response(json.dumps(obj))
  response.headers['Content-Type'] = 'application/json'
  return response 



@app.route('/deleteData/<USER_ID>/<TITLE>', methods=['POST'])
def deletedatafromdatabase(USER_ID,TITLE):

  print 'UserID %s' % USER_ID
  print 'TITLE %s' % TITLE


  if os.getenv('SERVER_SOFTWARE', '').startswith('Google App Engine/'):
    db = MySQLdb.connect(
        unix_socket='/cloudsql/{}:{}'.format(
          'digitaldiary-76dfb',
          'diary'),
          user='root',passwd = 'root',db='diarydb')
        # When running locally, you can either connect to a local running
        # MySQL instance, or connect to your Cloud SQL instance over TCP.
  else:
    db = MySQLdb.connect(host='localhost', user='root', passwd = 'root', db = 'diarydb')

  cursor = db.cursor()

  try:
    cursor.execute("""delete from diary where userID = '%s' and diaryTITLE = '%s'""" % (USER_ID,TITLE))
    db.commit()
  except:
    db.rollback() 

  

  db.close()
 
  response = make_response(json.dumps("deleted"));
  response.headers['Content-Type'] = 'application/json'
  return response 



if __name__ == '__main__':
  app.debug = True
  app.run(host='0.0.0.0', port=4567)
