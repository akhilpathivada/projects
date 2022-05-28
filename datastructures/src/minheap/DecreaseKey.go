package minheap

import "log"

func decreaseKey(heapArray []int, index int, key int) {
	if key > heapArray[index] {
		log.Panic("ERROR")
	}
	heapArray[index] = key

	for index >= 0 && heapArray[(index-1)/2] > heapArray[index] {
		swapHeapArrayElements(&heapArray[index], &heapArray[(index-1)/2])
		index = (index - 1) / 2
	}
}
