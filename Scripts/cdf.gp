reset
set terminal pdf enhanced dashed
set output 'd2d-search-cdf.pdf'

set key right top
#set xrange[10:100000]
#set yrange[0.4:1]

#set format x "10^{%L}"
#set format y "10^{%L}"
#set logscale x;


set ylabel 'P(Time to find the device > T)'
set xlabel 'Time(ms)'

set style line 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;
set style line 3 lc rgb 'green' lt 3 lw 3 ps 1 dashtype 3;
set style line 4 lc rgb 'dark-yellow' lt 4 lw 3 ps 1 dashtype 4;
set style line 5 lc rgb 'pink' lt 5 lw 3 ps 1 dashtype 5;
set style line 6 lc rgb 'orange' lt 6 lw 3 ps 1 dashtype 6;

plot 'data/search0.hist' using 1:4 w l ls 1  title '0m', \
     'data/search1.hist' using 1:4 w l ls 2  title '1m', \
     'data/search3.hist' using 1:4 w l ls 3  title '3m', \
     'data/search5.hist' using 1:4 w l ls 4  title '5m', \
     'data/search10.hist' using 1:4 w l ls 5  title '10m', \
     'data/search15.hist' using 1:4 w l ls 6  title '15m'


reset
set terminal pdf enhanced dashed
set output 'd2d-connect-cl-cdf.pdf'

set key right top

set ylabel 'P(Client Connection Time > T)'
set xlabel 'Time(ms)'

set style line 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;
set style line 3 lc rgb 'green' lt 3 lw 3 ps 1 dashtype 3;
set style line 4 lc rgb 'dark-yellow' lt 4 lw 3 ps 1 dashtype 4;
set style line 5 lc rgb 'pink' lt 5 lw 3 ps 1 dashtype 5;
set style line 6 lc rgb 'orange' lt 6 lw 3 ps 1 dashtype 6;

plot 'data/connectC0.hist' using 1:4 w l ls 1  title '0m', \
     'data/connectC1.hist' using 1:4 w l ls 2  title '1m', \
     'data/connectC3.hist' using 1:4 w l ls 3  title '3m', \
     'data/connectC5.hist' using 1:4 w l ls 4  title '5m', \
     'data/connectC10.hist' using 1:4 w l ls 5  title '10m', \
     'data/connectC15.hist' using 1:4 w l ls 6  title '15m'

reset
set terminal pdf enhanced dashed
set output 'd2d-connect-go-cdf.pdf'

set key right top

set ylabel 'P(Tempo de Conexão GO > T)'
set xlabel 'Tempo (ms)'

set style line 1 lc rgb 'blue' lt 1 lw 3;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1;
set style line 3 lc rgb 'green' lt 3 lw 3 ps 1;
set style line 4 lc rgb 'dark-yellow' lt 4 lw 3 ps 1;
set style line 5 lc rgb 'pink' lt 5 lw 3 ps 1;
set style line 6 lc rgb 'orange' lt 6 lw 3 ps 1;

plot 'data/connectG0.hist' using 1:4 w l ls 1  title '0m', \
     'data/connectG1.hist' using 1:4 w l ls 2  title '1m', \
     'data/connectG3.hist' using 1:4 w l ls 3  title '3m', \
     'data/connectG5.hist' using 1:4 w l ls 4  title '5m', \
     'data/connectG10.hist' using 1:4 w l ls 5  title '10m', \
     'data/connectG15.hist' using 1:4 w l ls 6  title '15m'

##Vazao Local
reset
set terminal pdf enhanced dashed
set output 'd2d-LB-local-cdf.pdf'

#set xrange[10:100000]
#set yrange[0.4:1]

set format x "10^{%L}"
#set format y "10^{%L}"
set logscale x;

set key left top

set ylabel 'P(Bandwidth > x)'
set xlabel 'Bandwidth (KB/s)'

set style line 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;


plot 'data/CDF-LB-DOWNLOAD-SERVER_LOCAL.hist' using 1:4 w l ls 1  title 'Download', \
     'data/CDF-LB-UPLOAD-SERVER_LOCAL.hist' using 1:4 w l ls 2  title 'Upload'



###Vazao Server Extern
reset
set terminal pdf enhanced dashed
set output 'd2d-LB-server-cdf.pdf'

#set xrange[10:100000]
#set yrange[0.4:1]

set format x "10^{%L}"
#set format y "10^{%L}"
set logscale x;

set key left top

set ylabel 'P(Bandwidth > x)'
set xlabel 'Bandwidth (KB/s)'

set style line 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;


plot 'data/CDF-LB-DOWNLOAD-SERVER_EXTERNAL.hist' using 1:4 w l ls 1  title 'Download', \
     'data/CDF-LB-UPLOAD-SERVER_EXTERNAL.hist' using 1:4 w l ls 2  title 'Upload'



###Vazão D2D
reset
set terminal pdf enhanced dashed
set output 'd2d-lb-cdf.pdf'

set ylabel 'P(Bandwidth > x)'
set xlabel 'Bandwidth (KB/s)'

set key left top
#set xtics rotate by 270

#set format x "10^{%L}"
#set logscale x;

set style line 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;
set style line 3 lc rgb 'green' lt 3 lw 3 ps 1 dashtype 3;
set style line 4 lc rgb 'dark-yellow' lt 4 lw 3 ps 1 dashtype 4;
set style line 5 lc rgb 'pink' lt 5 lw 3 ps 1 dashtype 5;
set style line 6 lc rgb 'orange' lt 6 lw 3 ps 1 dashtype 6;

plot	'data/CDF-LB-D2D-0.hist' using 1:4 w l ls 1  title '0m', \
	'data/CDF-LB-D2D-1.hist' using 1:4 w l ls 2  title '1m', \
	'data/CDF-LB-D2D-3.hist' using 1:4 w l ls 3  title '3m', \
	'data/CDF-LB-D2D-5.hist' using 1:4 w l ls 4  title '5m', \
	'data/CDF-LB-D2D-10.hist' using 1:4 w l ls 5  title '10m', \
	'data/CDF-LB-D2D-15.hist' using 1:4 w l ls 6  title '15m'

##RTT
reset
set terminal pdf enhanced dashed
set output 'd2d-rtt-cdf.pdf'

#set xrange[10:100000]
#set yrange[0.4:1]

set format x "10^{%L}"
#set format y "10^{%L}"
set logscale x;

set key right top

set ylabel 'P(RTT > x)'
set xlabel 'RTT (ms)'

set style line 1 lc rgb 'blue' lt 1 lw 3;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1;

plot 'data/CDF-RTT-LOCAL.hist' using 1:4 w l ls 1  title 'Server-Local', \
     'data/CDF-RTT-SERVER.hist' using 1:4 w l ls 2  title 'Server External'

