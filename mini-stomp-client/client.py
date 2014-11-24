import time
import sys
import logging
import threading
import sqlite3

from stompclient import PublishSubscribeClient

logging.basicConfig(level=logging.INFO)

db = sqlite3.connect("stomp-records.sqlite")

def frame_received(frame):
  mid = frame.headers["message-id"]
  logging.info(mid)
  body = frame.body

  db.execute("INSERT INTO input_kills (timestamp, messageid, json) VALUES (NOW(), ?, ?)", (mid, body))
  logging.info("Inserted")

logging.info("Starting")
client = PublishSubscribeClient('eve-kill.net', 61613)
listener = threading.Thread(target=client.listen_forever)
listener.start()

# For our example, we want to wait until the server is actually listening
client.listening_event.wait()

client.connect('guest', 'guest')
client.subscribe("/topic/kills", frame_received)
while True:
  time.sleep(200)
