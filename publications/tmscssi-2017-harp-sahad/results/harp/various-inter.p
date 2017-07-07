set terminal postscript eps enhanced color "Times-Roman" 25 
set size 1,1 
set xlabel "number of nodes pulled from 1 partition to another"
set ylabel "number of edges"

set key top right
#set xrange [0:0.5]
#set yrange [0:0.2]
set xtics 20000

plot  'various-inter.result' using ($2):($3) title "intra partition" w l lw 4, \
'various-inter.result' using ($2):($4) title "inter partition" w l lw 4

set output 'various-inter.eps'

replot 
