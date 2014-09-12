### Bencodej

A Java Bencode Library. The goal of this implementation was to properly separate the bencoding representation from the
data structure which can be encoded, that is compositions of integers, byte strings, lists and dictionaries. There is full
test coverage, and consulting the tests can be a useful guide to using the library.

## Install

The easiest way to install the library in a Java project is to include it as a dependency in your POM file.
```
 <dependencies>
    ...
    <dependency>
      <groupId>org.bencodej</groupId>
      <artifactId>core</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
    ...
 </dependencies>
```

## Usage

Decoded bencodings are returned as objects from classes implementing the Bencodable interface, of which there are three such classes
- BencodableInteger
- BencodableByteString
- BencodableList
- BencodableDictionary

where the last two are also extend the linked list and hash map classes respectively. Hence one can create Bencodable objects as such
```
BencodableDictionary b = new BencodableDictionary();
b.put(new BencodableByteString("firstKey".getBytes(), new BencodableInteger(33));
b.put(new BencodableByteString("secondKey".getBytes(), new BencodableByteString("secondValue".getBytes()));
```
This object can then by bencoded by calling b.bencode(), which produces the bencoded string
```
d8:firstKeyi33e9:secondKey11:secondValuee
```
Conversely, to decode this string and recover the Bencodable object structure as above, just call
```
Bencodable o = Bencodej.decode("d8:firstKeyi33e9:secondKey11:secondValuee".getBytes());
```

## License
See LICENSE file.

## Author
* Bedeho Mender <<bedeho.mender@gmail.com>>
  Original author, main developer and maintainer