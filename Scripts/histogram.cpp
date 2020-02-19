/*
 * criado por fernando - nov 2006*/

#include<cstdio>
#include<cstdlib>
#include<map>
#include<vector>
#include <algorithm>
using namespace::std;

int main(int argc, char **argv){
   FILE *out;
   map<double,double> count;
   double total = 0;
   double sample = 0;
   vector<double> list;
   
   if(argc != 2){
      fprintf(stderr,"usage: %s <fileout.hist>\n", argv[0]);
      exit(EXIT_FAILURE);
   }
   
   //lendo dados da entrada, uma amostra double por linha
   while( scanf("%lf", &sample)==1 ){
      total++;
      if(count.find(sample) == count.end() ){
         count[sample] = 1;
         list.push_back(sample);
      }else{
         count[sample]++;
      }
   }
   
   // sorting samples ..smallest to biggest
   sort(list.begin(), list.end());
   
   //arquivo de saida .hist
   if(! (out=fopen(argv[1],"w")) ){
      fprintf(stderr,"error: cannot create %s\n",argv[1]);
      exit(EXIT_FAILURE);
   }
   
   double freq = 0;
   double pdf = 0;
   double cdf = 0;
   double ccdf = 0;
   for(int i=0; i<list.size(); i++){
      freq = count[ list[i] ];
      pdf = freq/total;
      cdf += pdf;
      ccdf = 1-cdf;
      fprintf(out, "%lf %lf %lf %f %f\n", list[i], freq, pdf, cdf, ccdf);
   }


   fclose(out);
   return 0;
}
