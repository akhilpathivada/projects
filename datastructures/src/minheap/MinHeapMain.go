package minheap

import "fmt"

func MinHeapMain() {
	heapArray, size := constructMinHeap()
	fmt.Println("Actual MinHeap : ", heapArray)

	minElementOfMinHeap := extractMinElementFromMinHeap(heapArray, &size)
	fmt.Println("Minimum element of MinHeap : ", minElementOfMinHeap)
	fmt.Println("MinHeap after extracing min. element : ", heapArray[:size])

	// Decreasing value of element at index-2 to 0
	decreaseKey(heapArray, 2, 0)
	fmt.Println("MinHeap after decreasing an element : ", heapArray[:size])
}
