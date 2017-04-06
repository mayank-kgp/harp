#!/usr/bin/python
import sys, os

if len(sys.argv) != 3:
	print 'USAGE: ' + sys.argv[0] + ' <application-log-folder> <num of nodes>'
	sys.exit()

app_log_folder = sys.argv[1]
numNode = sys.argv[2]

for container in os.listdir(app_log_folder):
	container_id = container[-4:]
	if container_id == '0001':
		continue
	fsyslog = app_log_folder+ "/" + container + "/syslog"
	print container_id
	print fsyslog
