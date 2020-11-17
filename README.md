# Assignment 1: Symmetric Encryption Using Diffie-Hellman Key Agreement


Program envoked by running.

 ```sh
java Assignment1 someFileToEncrypt > Encryption.txt
 ```



## Spec

The aim of this assignment is to perform symmetric encryption using the block cipher AES. Before this encryption can be done, a key must be exchanged with the receiver of the message (me); this will be done using Diffie-Hellman key agreement.

### Values which you need to know

The prime modulus p is the following 1024-bit prime (given in hexadecimal):

```
b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6eddef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323
```

The generator g is the following (again in hexadecimal):

```
44ec9d52c8f9189e49cd7c70253c2eb3154dd4f08467a64a0267c9defe4119f2e373388cfa350a4e66e432d638ccdc58eb703e31d4c84e50398f9f91677e88641a2d2f6157e2f4ec538088dcf5940b053c622e53bab0b4e84b1465f5738f549664bd7430961d3e5a2e7bceb62418db747386a58ff267a9939833beefb7a6fd68
```

My public shared value A for the Diffie-Hellman key change is given by ga (mod p) where a is my secret value. A has the following value:

```
5af3e806e0fa466dc75de60186760516792b70fdcd72a5b6238e6f6b76ece1f1b38ba4e210f61a2b84ef1b5dc4151e799485b2171fcf318f86d42616b8fd8111d59552e4b5f228ee838d535b4b987f1eaf3e5de3ea0c403a6c38002b49eade15171cb861b367732460e3a9842b532761c16218c4fea51be8ea0248385f6bac0d
```

### Steps

In order to perform the Diffie-Hellman key exchange, you should do the following:

1. Generate a random 1023-bit integer; this will be your secret value b.
2. Generate your public shared value B given by gb (mod p)
3. Calculate the shared secret s given by Ab (mod p)

Now that you have the value of the shared secret s, you can use this for your AES encryption. However, it is too large (1024 bits) to be used directly as the AES key. You should therefore use SHA-256 to produce a 256-bit digest from the shared secret s, giving a 256-bit AES key k.

You will then encrypt an input binary file using AES in CBC mode with the 256-bit key k and a block size of 128-bits. The IV for this encryption will be a randomly generated 128-bit value. You will use the following padding scheme (as given in lectures): if the final part of the message is less than the block size, append a 1-bit and fill the rest of the block with 0-bits; if the final part of the message is equal to the block size, then create an extra block starting with a 1-bit and fill the rest of the block with 0-bits.

### Implementation

* The implementation language must be Java.
    * Your program should take an additional filename in the command line and output to standard output the result of encrypting this file. The input binary file will be the Java class file resulting from compiling your program.

* You will have to make use of the following classes.
    * BigInteger class (java.math.BigInteger),
        * You must not make use of the methods provided by the BigInteger class to implement the modular multiplication;
    * the security libraries (java.security.*) and
    * the crypto libraries (javax.crypto.*).
        * You can make use of the crypto libraries to perform the AES encryption and the SHA-256 hashing.

* all modular exponentiation must be done using one of the two square and multiply algorithms described in the lectures (left-to-right method or right-to-left method).


### Deliverable Format

Produce a ".zip" file containing.

1. DH.txt - your 1024-bit shared Diffie-Hellman public value B in hexadecimal (256 hex digits with no white space).
2. IV.txt - your 128-bit IV in hexadecimal (32 hex digits with no white space).
3. Assignment1.java - your program code file.
4. Assignment1.class - the result of compiling the above code file, which was encrypted.
5. Encryption.txt - Producuced by running `java Assignment1 Assignment1.class`.
6. A declaration that this is solely your own work (except elements that are explicitly attributed to another source).


### Due Date

This assignment is due 10am on Monday 23rd November. This assignment carries 15 marks and late submissions will be penalised 1.5 marks for each 24 hours the assignment is overdue.

### Common Mistakes

1. Calculating the shared secret value incorrectly (e.g. A^B (mod p) rather than A^b (mod p)).
2. Calculating the AES key from the shared value using the character values in the string representation rather than the actual byte values.
3. Converting BigInteger values to an array of bytes incorrectly - note that the BigInteger method toByteArray() uses a twos complement representation and may add an extra leading zero-valued byte if the first bit is set.
4. Padding incorrectly: none of the padding mechanisms provided by the Java libraries corresponds to the required padding; you will need to use the NoPadding option and implement the padding yourself.
5. Padding with the characters ‘0’ and '1' rather than the bit values 1 and 0..
6. Giving decimal values rather than hexadecimal.
7. Giving negative hex values.
