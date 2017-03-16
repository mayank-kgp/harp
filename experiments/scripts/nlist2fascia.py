#transfer nlist to fascia format

import os
import sys
#input = sys.argv[1]
#outfile = sys.argv[2]
input = "test.nlist"
outfile ="test.fascia"
f = open(input, 'r')
out = open(outfile, 'w')
m = 0
n = 0
for line in f:
    print (line.split("\t"))
    n = n + 1
    str = line.split("\t")
    srcid = int(str[0])
    distlist = str[1].rstrip("\n")
    print(distlist.split(","))
    for dist in distlist.split(","):
        try:
            distid = int(dist)
            if  srcid < distid:
                m = m + 1
                out.write("{0}\t{1}\n".format(srcid, distid))
        except Exception as e:
            None

out.close()
f.close()
tmpfile = input+'.tmp'
open(tmpfile, 'w').write('{0}\n{1}\n'.format(n,m) + open(outfile, 'r').read())
os.rename(tmpfile, 'test.fascia')



