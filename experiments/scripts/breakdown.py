#!/usr/bin/python
## used for analyze specific logs
## need to aggregate the logs on the cluster to one application-log-folder first.
import sys, os

if len(sys.argv) != 2:
	print 'USAGE: ' + sys.argv[0] + ' <application-log-folder> '
	sys.exit()

app_log_folder = sys.argv[1]
res=[]
for container in os.listdir(app_log_folder):
	container_id = container[-4:]
	if container_id == '0001':
		continue
	fsyslog = app_log_folder+ "/" + container + "/syslog"
	ares = []
	f = open(fsyslog, 'r')
	for line in f:
		if 'Loaded graph' in line:
			load_graph = line.rsplit(None, 1)[-1]
			ares.append(load_graph)
			#print (load_graph)
		elif 'color graph takes' in line:
			color_graph = line.rsplit(None, 1)[-1]
			ares.append(color_graph)
			#print (color_graph)
		elif '[END] SCCollectiveMapper.matchSubTemplateMultiThread.' in line:
			rotate_or_compute = line.rsplit(None, 1)[-1]
			ares.append(rotate_or_compute)
			#print (rotate_or_compute)
		elif 'allreduce is done.takes' in line:
			allreduce = line.rsplit(None, 1)[-1]
			ares.append(allreduce)
			#print(allreduce)
		elif 'is done.takes' in line and 'size' in line:
			done = line.rsplit(None, 2)[-2].strip(';')
			ares.append(done)
			#print(done)
        res.append((container_id, ares) ) 
	f.close()

# output
for (id, list) in res:
	print ("{0}\t{1}".format(id,'\t'.join(list) ))







