# URLShortner

#Overview
Url shortener service is used to share shortened url via SMS(saves valuable characters
in it) or link via Mail(easy to share) etc. In this project, I have created a service which
primarily shortens url for a given long url and give long url for a short url. SHA-256
algorithm is used to achieve unique short URLs along with expiration time for each short
url to exploit reusability and space constraints. Apart from this, analysis pertaining to
clicks and total requests/API calls were also undertaken.

#Goals
1. Shorten url with minimal or no repetitions.
2. Exploit reusability of short url, i.e. try using a short url for multiple long urls.
3. Analysis: Number of clicks for given short url, number of times services used.

#Technical Specifications
1. Language: JAVA
2. Framework: Spring
3. ORM: Hibernate
4. Database: MySQL
5. Caching: Redis
6. Uniqueness algorithm: Bucketed SHA-256
7. Server: Tomcat

#Why SHA-256 ?
1) Output of SHA-256 is 32 bytes, 64 characters (hexadecimal). Considering 8
characters, gives 36^8 = 3 trillion possibilities. Moreover the algorithm designed
covers much more than these possibilities because of reusability and increased
bucket size.
2) Reusability - Every shortURL can be attributed with expiry time. Once the
shortURL expires for given longURL, we can reuse it for some another longURL.
3) Decryption of shortURL is computationally impossible task.
4) Dynamic length of shortURL possible.
