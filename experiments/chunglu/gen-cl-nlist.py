#!/usr/bin/python

import sys, os

def AddUV(graph, u, v):
	if u not in graph:
		nei = []
		graph[u] = nei
	graph[u].append(v)

if len(sys.argv) != 2:
	print "USAGE: " + sys.argv[0] + " <nlist>"
	sys.exit()

gName = sys.argv[1].split('.')[0]
degName = gName + 'Deg.deg'

# compile chung lu generator
os.system('g++ *.cpp')

# add 10 degree each iteration
D = 10
for iterD in range(5):
	# generating degree sequence file
	print "=== producing degree list"
	fNlist = open(sys.argv[1])
	fDeg = open(degName, 'w+')

	n = 0
	for line in fNlist:
		n += 1
		if n % 1000 == 0: 
			print 'ITER ' + str(iterD) + \
					' generating degree seq: ' + str(n) + ' node have been processed'
		entry = line.split()
		if len(entry) <= 1:
			n -= 1
			continue
		deg = len(entry[1].split(',')) + D*iterD
		fDeg.write(str(deg) + '\n')

	fNlist.close()
	fDeg.close()
	os.system("sed -i '1s/^/" + str(n) + "\\n /' " + degName)

# generate Chung-Lu file
	print "=== producing chung lu uel"
	os.system("./a.out " + degName + " "  + gName + ".uel")


# generating chung-lu nlist
	print "=== producing chung lu nlist"
	n = 0
	fUel = open(gName + '.uel')
	graph = dict()
	for line in fUel:
		n += 1
		if n % 10000 == 0: 
			print 'ITER ' + str(iterD) + \
					' producing chung lu nlist: ' + str(n) + ' edges have been processed'
		uv = line.strip().split()
		u = int(uv[0])
		v = int(uv[1])
		AddUV(graph, u, v)
		AddUV(graph, v, u)
		
	fFinal = open(gName + '-' + str(iterD) + '-CL.nlist', 'w+')
	for u in graph:
		fFinal.write(str(u))
		fFinal.write(' ')
		for v in graph[u][:-1]:
			fFinal.write(str(v))
			fFinal.write(',')
		fFinal.write(str(graph[u][-1]))
		fFinal.write('\n')

	fFinal.close()

print 'COMPLETE'
