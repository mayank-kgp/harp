#!/usr/bin/python
import sys, os

if len(sys.argv) != 6:
	print 'USAGE: ' + sys.argv[0] + ' <input> <output> <repeat> <numOfTemplates> <numOfDiffNodes>'
	sys.exit()

inputs = sys.argv[1]
output = sys.argv[2]
repeat = int(sys.argv[3])
noOfTemplates=int(sys.argv[4])
noOfDiffNodes=int(sys.argv[5])

f = open(sys.argv[1], 'r')
out = open(sys.argv[2],'w')
line0=""
line1=""
line2=""
line3=""

nRows=noOfDiffNodes
nColumns=noOfTemplates * repeat

table = [["" for x in range(nColumns)] for x in range(nRows)]

column = -repeat
row = 0
count = 0
index =-1
for line in f:
	line0=line1
	line1=line2
	line2=line3
	line3=line
	if "Above experiment is" in line:
		if (count % (noOfTemplates*noOfDiffNodes) == 0):
			index = index + 1
			column = -repeat + index
		
		row = row % nRows
		
		if( row == 0):
			column = column + repeat					

		table[row][column]=line0.replace("\n","")
		
		row = row + 1		

		count = count + 1

f.close()


for i in range(nRows):
	cc = 0
	for j in range(nColumns):
		if( (j!=0) and (cc % repeat == 0)):
			out.write(" \t")
		out.write(table[i][j]+"\t")
		cc = cc + 1
	out.write("\n")


out.close()
