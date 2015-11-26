#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>

struct _min_stack
{
	size_t capacity;

	uint32_t size;
	uint32_t* elements;

	uint32_t min;
	uint32_t* mins;
};

typedef struct _min_stack min_stack_s;

typedef struct _min_stack* min_stack_t;

static min_stack_t
min_stack_new(uint32_t capacity)
{
	min_stack_t res = (min_stack_t) malloc (sizeof (min_stack_s));
	if (res == NULL)
		return NULL;

	res->elements = NULL;
	res->elements = (uint32_t*) malloc (capacity * sizeof(uint32_t));
	if (res->elements == NULL) {
		free (res);
		return NULL;
	}

	res->mins = NULL;
	res->mins = (uint32_t*) malloc (capacity * sizeof(uint32_t));
	if (res->mins == NULL)
	{
		free (res->elements);
		free (res);
		return NULL;
	}

    for (int i = 0; i < capacity; i++)
        res->mins[i] = 0;

	res->capacity = capacity;
	res->min = 0;
	res->size = 0;

	return res;
}

static inline void
min_stack_delete(min_stack_t stack)
{
    free (stack->elements);
    free (stack->mins);
    free (stack);
}

static bool
min_stack_push(min_stack_t stack, uint32_t element)
{
    if (stack == NULL)
        return false;

    if (stack->capacity <= stack->size)
        return false;

    stack->elements[stack->size++] = element;

    if (stack->min == 0 || stack->mins[stack->min - 1] > element)
        stack->mins[stack->min++] = element;

    printf("=== %d\n", stack->min);

    return true;
}

static inline bool
min_stack_pop(min_stack_t stack, uint32_t* res)
{
    if (stack == NULL || res == NULL)
        return false;

    *res = stack->elements[--(stack->size)];

    if (*res == stack->mins[stack->min - 1])
        stack->min--;

    return true;
}

static inline bool
min_stack_min(min_stack_t stack, uint32_t* res)
{
    if (stack == NULL || res == NULL)
        return false;

    if (stack->size == 0)
        return false;

    *res = stack->mins[stack->min - 1];

    return true;
}

static void
min_stack_print(min_stack_t stack)
{
    if (stack == NULL)
        return;

    printf("values:\n");
    for (int i = 0; i < stack->size; i++)
        printf("%d ", stack->elements[i]);

    printf("\n");

    printf("mins:\n");
    for (int i = 0; i < stack->min; i++)
        printf("%d ", stack->mins[i]);

    printf("\n");
}

int main(int argc, char** argv)
{
	min_stack_t min = min_stack_new(1024);

    min_stack_push(min, 1);
    min_stack_push(min, 2);
    min_stack_push(min, 0);

    min_stack_print(min);

    uint32_t res;

    min_stack_min(min, &res);

    printf("%d\n", res);

    min_stack_pop(min, &res);

    printf("%d\n", res);

    min_stack_print(min);

    min_stack_min(min, &res);

    printf("%d\n", res);

    min_stack_delete(min);

    return 0;
}
