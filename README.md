KDownloader
===========

This repository contains the code for the `SSC B: Multithreaded File Downloader`. This GUI client allows the user to select a number of extensions, a website, and a destination folder, to which the specified links from the specified website will be downloaded. The user can also select the number of threads used to download the files.

![demo](http://recordit.co/9qJXx7mlnk)

Naming Convention
-----------------
Throughout my code I use the variable identifier `svar`, which is the Norweigan word for `answer`. For example, if I am finding the sum of the elements of an array, I will usually do something like the following:

```java
public static int sum(int[] arr) {
  int svar = 0;
  for (int i = 0; i < arr.length; ++i) {
    svar += arr[i];
  }

  return svar;
}
```


