//
// Created by frank515 on 22-11-27.
//
#include <stdexcept>
#include <cstring>

extern const size_t DEF_MEM_LEN;

class Memory{
public:
    std::vector<unsigned char > *mem;
    size_t size;
    size_t last;

    Memory() : size(DEF_MEM_LEN) {};

    void make(size_t i = DEF_MEM_LEN) {
        mem = new std::vector<unsigned char >(i, 0);
        this->size = i;
    }

    ~Memory() {
        delete mem;
    }

    unsigned char at(size_t loc) {
        last = loc;
        if (loc < size) return mem->operator[](loc);
        else throw std::logic_error("Error: Read/Write uninitialized memory");
    }

    void next() {
        if (last + 1 <= size) last++;
    }

    void next(size_t i) {
        if (last + i <= size) last += i;
        else throw std::logic_error("Error: Read/Write uninitialized memory");  // Will this run correctly?
    }


    int32_t getInt() {
        //return *reinterpret_cast<int *>((void *) last);
        int32_t i = ((int)mem->at(last + 0) << 24) +
                    ((int)mem->at(last + 1) << 16) +
                    ((int)mem->at(last + 2) << 8)  +
                    ((int)mem->at(last + 3) << 0);
        return i;
    }

    int32_t getInt(size_t loc) {
        //return *reinterpret_cast<int *>((void *) last);
        int32_t i = ((int)mem->at(loc + 0) << 24) +
                    ((int)mem->at(loc + 1) << 16) +
                    ((int)mem->at(loc + 2) << 8)  +
                    ((int)mem->at(loc + 3) << 0);
        return i;
    }

    void setInt(int32_t i) {
        //*(static_cast<int*>((void*)last)) = i;
        mem->at(last + 0) = static_cast<unsigned char >((i << 0) >> 24);
        mem->at(last + 1) = static_cast<unsigned char >((i << 8) >> 24);
        mem->at(last + 2) = static_cast<unsigned char >((i << 16) >> 24);
        mem->at(last + 3) = static_cast<unsigned char >((i << 24) >> 24);
    }

    void setChar(char c) {
        mem->at(last) = c;
    }

    char getChar() {
        return static_cast<int>(mem->at(last));
    }

    char getChar(size_t loc) {
        return static_cast<int>(mem->at(loc));
    }

    unsigned char& get(size_t loc) {
        last = loc;
        return mem->at(loc);
    }

    unsigned char& get() {
        return mem->at(last);
    }
};

