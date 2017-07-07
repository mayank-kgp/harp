set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set yrange [-0.05:0.05]
set xrange [1:30]
unset key
min_y = -0.06082381315 
max_y = 0.0311128419 
f(x) = mean_y
fit f(x) 'harp-30-webgoogle-percent.data' u 1:2 via mean_y

stddev_y = sqrt(FIT_WSSR / (FIT_NDF + 1 ))

# Plotting the range of standard deviation with a shaded background
set label 1 gprintf("Standard deviation = %g", stddev_y) at 8, 0.04
set output 'harp-30-webgoogle-percent.eps'

plot mean_y-stddev_y with filledcurves y1=mean_y lt 1 lc rgb "grey", \
mean_y+stddev_y with filledcurves y1=mean_y lt 1 lc rgb "grey", \
mean_y w l lt 3, 'harp-30-webgoogle-percent.data' u 1:2 w p pt 7 lt 1 ps 1

