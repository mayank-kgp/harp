#!/usr/bin/python
import sys, os

if len(sys.argv) != 3:
	print 'USAGE: ' + sys.argv[0] + ' <input> <output>'
	sys.exit()

inputs = sys.argv[1]
output = sys.argv[2]

f = open(sys.argv[1], 'r')
out = open(sys.argv[2],'w')
line0=""
line1=""
line2=""
line3=""


for line in f:
	line0=line1
	line1=line2
	line2=line3
	line3=line
	if "Above experiment is" in line:
		out.write(line0+line2+line3+"=====================\n")
f.close()
out.close()
