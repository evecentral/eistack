import time
import sys
import logging
import threading
logging.basicConfig(level=logging.DEBUG)

from stompclient import PublishSubscribeClient

def frame_received(frame):
    # Do something with the frame!
    print "----Received Frame----\n%s\n-----" % frame

client = PublishSubscribeClient('82.221.99.197', 61613)
listener = threading.Thread(target=client.listen_forever)
listener.start()

# For our example, we want to wait until the server is actually listening
client.listening_event.wait()

client.connect('guest', 'guest')
client.subscribe("/topic/kills", frame_received)
time.sleep(200000)
