package trie

const (
	ALPHABET_SIZE = 26
)

type TrieNode struct {
	childrens [ALPHABET_SIZE]*TrieNode
	isWordEnd bool
}
