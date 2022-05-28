package minheap

import "log"

func extractMinElementFromMinHeap(heapArray []int, size *int) int {
	if *size < 1 {
		log.Panic("UNDERFLOW")
	}
	minElement := heapArray[0]
	heapArray[0] = heapArray[(*size)-1]
	*size--
	minHeapify(heapArray, *size, 0)

	return minElement
}
