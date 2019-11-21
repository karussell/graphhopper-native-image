#include <iostream>
#include "libgraphhopper.h"
#include "graal_isolate.h"

using namespace std;

int main(int argc, char **argv) {
  // This is some Graal boilerplate code
  graal_isolatethread_t *thread = NULL;
  if (graal_create_isolate(NULL, NULL, &thread) != 0) {
        fprintf(stderr, "graal_create_isolate error\n");
        return 1;
  }
  // End boilerplate
  if(argc != 4 + 1) {
     std::cerr << "please specify coordinates\n./example latStart lonStart latEnd lonEnd" << std::endl;
     return 1;
  }

  double lat1 = stod(argv[1]);
  double lon1 = stod(argv[2]);
  double lat2 = stod(argv[3]);
  double lon2 = stod(argv[4]);
  double distance = runGH(thread, lat1, lon1, lat2, lon2);

  std::cout << "Distance " << distance << std::endl;

  // Clean up Graal stuff
  if (graal_detach_thread(thread) != 0) {
     fprintf(stderr, "graal_detach_thread error\n");
     return 1;
  }

  return 0;
}