#include <any>
#include <string>
#include "ast/AST.hpp"
#include "parse/driver.hh"
Enviroment *env;
Memory *mem;
AST::ExprNode *root;

// Memory default configuration, Unit byte;
const size_t DEF_MEM_LEN = 8192;      //total memory 8KB
const size_t RZV_MEM_LEN = 128;
const size_t STK_SIZE = DEF_MEM_LEN / 8;

// Return value will pass by memory on a selected address
// Maybe use a variable like a register will be better?
const size_t RETURN_VAL = 12;

string FileName = "a.ez";
using namespace std;

void setMemory() {
    // Initialize memory to zeros
    mem = new Memory();
    mem->make();
    env = new Enviroment();

    // Pass the memory information to mem;
    // Total mem size
    mem->at(0); mem->setInt(DEF_MEM_LEN);
    // Stack begin
    mem->at(4); mem->setInt(RZV_MEM_LEN);
    // Heap begin
    mem->at(8); mem->setInt(STK_SIZE);

}

int main(int argc, char **argv)
{

    if (argc > 1) {
        for (int i = 0; i < argc; i++) {
            FileName = string(argv[1]);
        }
    }

    setMemory();

    parse::Driver driver;
    auto sign = driver.parse_file(FileName);
    return sign;
}
