package minheap

import (
	"fmt"
	"log"
)

func swapHeapArrayElements(aptr *int, bptr *int) {
	temp := *aptr
	*aptr = *bptr
	*bptr = temp
}

func minHeapify(heapArray []int, size int, index int) {
	smallest := index
	left := (2 * index) + 1
	right := (2 * index) + 2

	if left < size && heapArray[left] < heapArray[smallest] {
		smallest = left
	}
	if right < size && heapArray[right] < heapArray[smallest] {
		smallest = right
	}
	if smallest != index {
		swapHeapArrayElements(&heapArray[index], &heapArray[smallest])
		minHeapify(heapArray, size, smallest)
	}
}

func convertToMinHeap(heapArray []int, size int) {
	for i := (size / 2) - 1; i >= 0; i-- {
		minHeapify(heapArray, size, i)
	}
}

func constructMinHeap() ([]int, int) {
	inputArray := []int{6, 5, 3, 7, 2, 8}
	fmt.Println("Initial Array : ", inputArray)
	size := len(inputArray)

	convertToMinHeap(inputArray[:], size)
	log.Print("Array was converted into MinHeap")

	return inputArray, size
}
