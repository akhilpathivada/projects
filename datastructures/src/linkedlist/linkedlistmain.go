package linkedlist

import "fmt"

/* Version-1 */
func displayLinkedList(head *LinkedListNode) {
	for head != nil {
		fmt.Printf("%v ->", head.Data)
		head = head.Next
	}
}

/* Version-2 */
func (head *LinkedListNode) displayLinkedList() {
	for head != nil {
		fmt.Printf("%v ->", head.Data)
		head = head.Next
	}
}

/* Reversing a Linked List */
func reverseLinkedList(headAddress **LinkedListNode) {
	currentPointer := *headAddress
	var prevPointer, NextPointer *LinkedListNode = nil, nil

	for currentPointer != nil {
		NextPointer = currentPointer.Next
		currentPointer.Next = prevPointer
		prevPointer = currentPointer
		currentPointer = NextPointer
	}

	*headAddress = prevPointer
}

/* Creating a new Linked List Node */
func newLinkedListNode(value int) *LinkedListNode {
	return &LinkedListNode{
		Data: value,
		Next: nil,
	}
}

/* Inserting into Linked List */
func insertToLinkedList(head *LinkedListNode, value int) *LinkedListNode {
	newNode := newLinkedListNode(value)
	newNode.Next = head
	return newNode
}

/* Creating a new Linked List */
func createLinkedList(headAddress **LinkedListNode) {

	for i := 1; i <= 5; i++ {
		*headAddress = insertToLinkedList(*headAddress, i)
	}
}

// LinkedListMain is ...
func LinkedListMain() {

	var headAddress *LinkedListNode
	createLinkedList(&headAddress)

	fmt.Print("\n Linked List :  ")
	headAddress.displayLinkedList() // Calling Version-2

	reverseLinkedList(&headAddress)

	fmt.Print("\n Reverse Linked List : ")
	displayLinkedList(headAddress) // Calling Version-1
}
