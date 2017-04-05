## Words counter
The program calculates the total number of words on the given page and the number of repetitions for each word.

## Code Example
    public static Map<String, Long> getWordsSortedMap(List<String> wordsList) {
        return wordsList.stream()
                .collect(
                        Collectors.groupingBy(
                                Function.identity(),
                                TreeMap::new,
                                Collectors.counting())
                );
    }
    
## Launching
You can run the program from your favorite environment. Doing this, keep in mind that:
- the entry point to the program is the class Counter;
- url string should be passed as the first argument;
- all other arguments will be ignored.
