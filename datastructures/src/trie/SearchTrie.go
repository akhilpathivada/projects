package trie

import "fmt"

/* Searching a Word in a Trie */
func (root *TrieNode) searchWordInTrie(word string) bool {
	lengthOfWord := len(word)
	ptr := root

	for i := 0; i < lengthOfWord; i++ {
		index := word[i] - 'a'

		if ptr.childrens[index] == nil {
			return false
		}
		ptr = ptr.childrens[index]
	}
	return ptr.isWordEnd
}

/* Searching some words in Trie */
func (root *TrieNode) searchTrie() {

	wordsToSearch := []string{"sam", "john", "tim", "jose", "rose", "cat", "dog", "dogg", "roses", "rosess", "ans", "san"}

	for i := 0; i < len(wordsToSearch); i++ {
		found := root.searchWordInTrie(wordsToSearch[i])

		if found {
			fmt.Printf("Word \"%s\" found in trie\n", wordsToSearch[i])
		} else {
			fmt.Printf("Word \"%s\" not found in trie\n", wordsToSearch[i])
		}
	}
}
