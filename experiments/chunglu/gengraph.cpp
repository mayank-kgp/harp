
/* 
 * File:   gengraph.hpp
 * Author: Maleq Khan
 *
 * Created on June 13, 2010
 *
 * generata graph using various models: Chung-Lu, G(n,p), etc. 
 */


#include "utility.hpp"
//#include "SGraph.hpp"
#include "Random.hpp"
#include <vector>

#ifndef NULL
	#define  NULL 0
#endif


using namespace std;


void ChungLu(int *dseq, int n, ofstream &outf) 
{
	// compute |E|
	long m = 0;
	for(int i = 0; i < n; i++)
	{
		m += dseq[i];
	}
	m = m/2;

	int i, ni, mi, k, nk, mik,  j;
	int u, v;
	
	int maxdeg = FindMax(dseq, n);
	int *degdist = new int[maxdeg+1];	// +1 for counting zero degree	
	int *first_id = new int[maxdeg+1];

	for (i=0; i <= maxdeg; i++)
		degdist[i] = 0;
	
	for (i=0; i<n; i++) 
		++degdist[dseq[i]];

	first_id[0] = 0;
	for (i=1; i <= maxdeg; i++)
		first_id[i] = first_id[i-1] + degdist[i-1];

	
	//create edges in a group of nodes with same degree
	for (i=1; i <= maxdeg; i++) {
		ni = degdist[i];
		if (ni==0) continue;
		if (ni>1) {
			mi = Binomial(int(ni*(ni-1)/2), double(i*i)/(2*m));  //m = |E|
			for (j=0; j<mi; ) {
				u = rand() % ni + first_id[i]; 
				v = rand() % ni + first_id[i];
				if (u==v) continue;
				outf << u << "\t" << v << endl;
			//	outf << v << "\t" << u << endl;
				j++;
			}
		}
		
		for (k=i+1; k <= maxdeg; k++) {
			nk = degdist[k];
			mik = Binomial(int(ni*nk), double(i*k)/(2*m));
			for (j=0; j<mik; j++) {
				u = rand() % ni + first_id[i]; 
				v = rand() % nk + first_id[k];
				outf << u << "\t" << v << endl;
			//	outf << v << "\t" << u << endl;
			}
		}
	}
	
	delete [] degdist;
	delete [] first_id;
	return;
}

/*------------------------------------------------------------------------------------*/
int ReadDegreeSequenceFile(char *fname, int * &dseq)
{
	cout << "Reading degree sequence files" << endl;
	//degree sequence file 
	ifstream dseqfile(fname);		
	if(!dseqfile.is_open()) {
		cout << "Cannot open " << fname << endl;
		exit(1);	
	}
	
	int n;
	dseqfile >> n;
	dseq = new int[n];
	int i;
	for (i=0; i<n; i++) 
		dseqfile >> dseq[i];
	dseqfile.close();

	return n;
}

/*------------------------------------------------------------------------------------*/
int main(const int argc, char *argv[])
{    
	int n, m;
	int *dseq;
			
	time_t start, sec;

	if (argc != 3) {
		cout << "USAGE: "  << endl;
		cout << "To generate Chung Lu graph: " << endl;
		cout << "\t " << argv[0] << " <deg_seq file> <output_file>" << endl << endl;
		exit(0);
	}

	cout << endl;
	start = time(NULL);
	n = ReadDegreeSequenceFile(argv[1], dseq);
	sec = time(NULL) - start;
	cout << "Time to read files: " << sec / 60 << " min. " << sec % 60 << " sec." << endl;
	
	srand(time(NULL));

	start = time(NULL);
	ofstream outf(argv[2]);
	ChungLu(dseq, n, outf); 
	outf.close();
	sec = time(NULL) - start;
	cout << "Time of computation: " << sec / 60 << " min. " << sec % 60 << " sec." << endl;

	delete [] dseq;
	return 0;
}
