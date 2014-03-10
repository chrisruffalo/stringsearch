stringsearch
============

**StringSearch** is a simple to use, fast, and decently compact (we'll get to that) structure for storing, and matching against, a large number of strings.
The basic architecture is pretty much cribbed from [concurrent-trees](https://code.google.com/p/concurrent-trees/) because, let's face it, there are
a lot of similarities here.

The main divergence, and reason for creating **StringSearch**, is that **StringSearch** is designed to handle sets of values (instead of a single value
on a given node) and it is also designed to search for (customizable) wildcards.

Another difference is that **StringSearch** aims to support different types of concurrency: none, consistent view, and strict.

 * None: no concurrency is offered.  This type is NOT thread safe.
 * Consistent View: uses atomic swapping to offer thread safe operations.  Does not enforce any sort of value locking.  Does not enforce put/get ordering.  This aims to be similar to, if not exactly the same as, the concurrency provided by *concurrent-trees*.
 * Strict: Writes and reads are mutually exclusive (along branches) and are kept in strict order.


