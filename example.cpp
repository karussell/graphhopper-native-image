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

  double lat1 = stod(argv[1]);
  double lon1 = stod(argv[2]); // e.g. 52.5147, 13.3883
  double distance = runGH(thread, 52.5169, 13.3884, lat1, lon1);

  std::cout << "Distance " << distance << std::endl;

  // Clean up Graal stuff
  if (graal_detach_thread(thread) != 0) {
     fprintf(stderr, "graal_detach_thread error\n");
     return 1;
  }

  return 0;
}