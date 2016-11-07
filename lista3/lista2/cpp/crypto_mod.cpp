#include <openssl/aes.h>
#include <string.h>
#include <iostream>
#include <thread>
#include <vector>
 
char reverseTable[128] = {
  64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
  64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
  64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 62, 64, 64, 64, 63,
  52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 64, 64, 64, 64, 64, 64,
  64,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
  15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 64, 64, 64, 64, 64,
  64, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
  41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 64, 64, 64, 64, 64
};

char range[] = "0123456789abcdef";

void b64Decode(char *src, unsigned char *out) {
  int collectedBits = 0, acc = 0, k = 0, i;
  for (i = 0; i < strlen(src); i++) {
    char c = src[i];
    if (c == ' ' || c == '=') {
      continue;
    }
    c = (int)c;
    if (c > 127 || c < 0 || reverseTable[c] > 63) {
      fprintf(stderr, "This string have illegal characters!\n");
      return;
    }
    acc = (acc << 6) | reverseTable[c];
    collectedBits += 6;
    if (collectedBits >= 8) {
      collectedBits -= 8;
      out[k] = (char)((acc >> collectedBits) & 0xffu);
      k++;
    }
  
 } out[k] = '\0';
}

int hexToDec(char c) {
  int i;
  for (i = 0; i < strlen(range); i++) {
    if (c == range[i]) {
      return i;
    }
  }
  return 0;
}

void hexToBin(unsigned char *t, const char *hex){
  for (int i = 0; i < strlen(hex); i += 2) {
    int a = hexToDec(hex[i])*16 + hexToDec(hex[i+1]);
    t[i/2] = a;
  }
}

std::string lookForSpecialChars(unsigned char *str, int a, int b) {
  unsigned char c[] = {str[a], str[b]};
  if (c[0] == 0xc4 && c[1] == 0x84) {
    return "Ą";
  } else if (c[0] == 0xc4 && c[1] == 0x85) {
    return "ą";
  } else if (c[0] == 0xc4 && c[1] == 0x86) {
    return "Ć";
  } else if (c[0] == 0xc4 && c[1] == 0x87) {
    return "ć";
  } else if (c[0] == 0xc4 && c[1] == 0x98) {
    return "Ę";
  } else if (c[0] == 0xc4 && c[1] == 0x99) {
    return "ę";
  } else if (c[0] == 0xc5 && c[1] == 0x81) {
    return "Ł";
  } else if (c[0] == 0xc5 && c[1] == 0x82) {
    return "ł";
  } else if (c[0] == 0xc5 && c[1] == 0x83) {
    return "Ń";
  } else if (c[0] == 0xc5 && c[1] == 0x84) {
    return "ń";
  } else if (c[0] == 0xc3 && c[1] == 0x93) {
    return "Ó";
  } else if (c[0] == 0xc3 && c[1] == 0xb3) {
    return "ó";
  } else if (c[0] == 0xc5 && c[1] == 0x9a) {
    return "Ś";
  } else if (c[0] == 0xc5 && c[1] == 0x9b) {
    return "ś";
  } else if (c[0] == 0xc5 && c[1] == 0xb9) {
    return "Ź";
  } else if (c[0] == 0xc5 && c[1] == 0xba) {
    return "ź";      
  } else if (c[0] == 0xc5 && c[1] == 0xbb) {
    return "Ż";
  } else if (c[0] == 0xc5 && c[1] == 0xbc) {
    return "ż";
  }  
  return "0";
}

const char *createKey(std::vector<int> arr, char *suffix) {
  srand(time(NULL));
  std::string key;
  for (int i = 0; i < arr.size(); i++) {
    key += range[arr[i]];
  }
  key += suffix;
  return key.c_str(); 
}

bool isStringValid(unsigned char *str) {
  std::string result = "";
  int counter = 0;
  for (int i = 0; i < strlen((char*)str); i++) {
    if (str[i] >= 0 && str[i] <= 126) {
      if (str[i] == 10) {
        break;
      } else if (str[i] < 32) {
        continue;
      }
      result += str[i];
      counter++;
    } else if (str[i] > 126 && str[i] < 255 && str[i+1] > 126 && str[i+1] < 255) {
      std::string c = lookForSpecialChars(str, i, i+1);
      if (c != "0") {
        result += c;
        i++;
        counter++;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
  if (counter < 30) {
    return false;
  }
  std::cout << "\n" << result << std::endl;
  return true;
}

bool found = false;
time_t start;
int oldTime;
unsigned char msg[1024];
char *secret, *suffix, *thisIv;
int n = 0, prefixLength;
void findKey(std::vector<int> arr, int id) {
  unsigned char iv[AES_BLOCK_SIZE], key[AES_BLOCK_SIZE*2], out[1024];
  hexToBin(iv, thisIv);
  b64Decode(secret, msg);
  hexToBin(key, createKey(arr, suffix));
  AES_KEY aes_key;
  AES_set_decrypt_key(key, 256, &aes_key);
  AES_cbc_encrypt(msg, out, 256, &aes_key, iv, AES_DECRYPT);
  n++;
  int elapsed = (int)difftime(time(0), start);
  if (elapsed > 0 && id == 1 && oldTime != elapsed/60) {
    oldTime = elapsed/60;
    std::cout << "Time elapsed: " << oldTime << " min / number of checked keys: " << n << std::endl;
  }
  if (found) {
    return;
  } else {
    if (isStringValid(out)) {
      const char *thisKey = createKey(arr, suffix);
      std::cout << "Message found!\n\nTime elapsed: " << difftime(time(0), start) <<" s / size of prefix: " << arr.size() << " / n = " << n << " / key = " << thisKey << ".\n"  << std::endl;
      found = true;
      return;
    }
  }
}

void iterate(int t) {
  std::vector<int> arr;
  for (int c1 = 15-t; c1 >= 0; c1-=4) {
    if (found) return;
    else if (prefixLength > 1) {
      for (int c2 = 15; c2 >= 0; c2--) {
        if (found) return;
        else if (prefixLength > 2) {
          for (int c3 = 15; c3 >= 0; c3--) {
            if (found) return;
            else if (prefixLength > 3) {
              for (int c4 = 15; c4 >= 0; c4--) {
                if (found) return;
                else if (prefixLength > 4) {
                  for (int c5 = 15; c5 >= 0; c5--) {
                    if (found) return;
                    else if (prefixLength > 5) {
                      for (int c6 = 15; c6 >= 0; c6--) {
                        if (found) return;
                        else if (prefixLength > 6) {
                          for (int c7 = 15; c7 >= 0; c7--) {
                            if (found) return;
                            else if (prefixLength > 7) {
                              for (int c8 = 15; c8 >= 0; c8--) {
                                if (found) return;
                                else if (prefixLength > 8) {
                                  for (int c9 = 15; c9 >= 0; c9--) {
                                    if (found) return;
                                    else if (prefixLength > 9) {
                                      for (int c10 = 15; c10 >= 0; c10--) {
                                        if (prefixLength > 10) {
                                          return;   
                                        } else {
                                          arr.push_back(c1);
                                          arr.push_back(c2);
                                          arr.push_back(c3);
                                          arr.push_back(c4);
                                          arr.push_back(c5);
                                          arr.push_back(c6);
                                          arr.push_back(c7);
                                          arr.push_back(c8);
                                          arr.push_back(c9);
                                          arr.push_back(c10);
                                          findKey(arr, t);
                                          arr.clear();
                                        }
                                      }
                                    } else {
                                      arr.push_back(c1);
                                      arr.push_back(c2);
                                      arr.push_back(c3);
                                      arr.push_back(c4);
                                      arr.push_back(c5);
                                      arr.push_back(c6);
                                      arr.push_back(c7);
                                      arr.push_back(c8);
                                      arr.push_back(c9);
                                      findKey(arr, t);
                                      arr.clear();
                                    }
                                  }
                                } else {
                                  arr.push_back(c1);
                                  arr.push_back(c2);
                                  arr.push_back(c3);
                                  arr.push_back(c4);
                                  arr.push_back(c5);
                                  arr.push_back(c6);
                                  arr.push_back(c7);
                                  arr.push_back(c8);
                                  findKey(arr, t);
                                  arr.clear();
                                }
                              }                               
                            } else {
                              arr.push_back(c1);
                              arr.push_back(c2);
                              arr.push_back(c3);
                              arr.push_back(c4);
                              arr.push_back(c5);
                              arr.push_back(c6);
                              arr.push_back(c7);
                              findKey(arr, t);
                              arr.clear();
                            }
                          }     
                        } else {
                          arr.push_back(c1);
                          arr.push_back(c2);
                          arr.push_back(c3);
                          arr.push_back(c4);
                          arr.push_back(c5);
                          arr.push_back(c6);
                          findKey(arr, t);
                          arr.clear();
                        }
                      }
                    } else {
                      arr.push_back(c1);
                      arr.push_back(c2);
                      arr.push_back(c3);
                      arr.push_back(c4);
                      arr.push_back(c5);
                      findKey(arr, t);
                      arr.clear();
                    }
                  }
                } else {
                  arr.push_back(c1);
                  arr.push_back(c2);
                  arr.push_back(c3);
                  arr.push_back(c4);
                  findKey(arr, t);
                  arr.clear();
                }
              }             
            } else {
              arr.push_back(c1);
              arr.push_back(c2);
              arr.push_back(c3);
              findKey(arr, t);
              arr.clear();
            }
          }
        } else {
          arr.push_back(c1);
          arr.push_back(c2);
          findKey(arr, t);
          arr.clear();
        }
      }
    } else {
      arr.push_back(c1);
      findKey(arr, t);
      arr.clear();
    }
  }
}

int main(int argc, char *argv[]) {
  if (argc != 4) {
    printf("\nError! 3 parameters required:\n- iv;\n- suffix of key;\n- ciphertext.\n\n");
    return 0;
  }

  int nt = 4;//8;
  secret = argv[3];
  suffix = argv[2];
  thisIv = argv[1];
  start = time(0);
  prefixLength = 64-strlen(suffix);

  std::thread t[nt];
  for (int i = 0; i < nt; i++) {
    t[i] = std::thread(iterate, i);
  }
  for (int i = 0; i < nt; i++) {
    t[i].join();
  }

  return 0;
}