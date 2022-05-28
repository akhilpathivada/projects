package trie

/* Insert words into Trie */
func (root *TrieNode) insertWordToTrie(word string) {
	lengthOfWord := len(word)
	ptr := root

	for i := 0; i < lengthOfWord; i++ {
		index := word[i] - 'a'

		if ptr.childrens[index] == nil {
			ptr.childrens[index] = &TrieNode{}
		}
		ptr = ptr.childrens[index]
	}
	ptr.isWordEnd = true
}

/* Construct Trie with some words */
func (root *TrieNode) constructTrie() {

	wordsToInsert := []string{"sam", "john", "tim", "jose", "rose", "cat", "dog", "dogg", "roses"}

	for i := 0; i < len(wordsToInsert); i++ {
		root.insertWordToTrie(wordsToInsert[i])
	}
}
