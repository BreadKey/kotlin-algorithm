from typing import List


def calculate(heights: List[int]) -> int:
    return sum([_trap_water(valley) for valley in _capture_vallies(heights)])


def _capture_vallies(heights: List[int]) -> List[List[int]]:
    result = []
    is_descending = True

    valley = []
    for height in heights:
        valley.append(height)

        if len(valley) > 1:
            if is_descending:
                if valley[-2] < valley[-1]:
                    is_descending = False

            if not is_descending:
                if valley[-1] >= valley[0]:
                    result.append(valley)
                    valley = valley[-1:]
                    is_descending = True

                elif valley[-1] < valley[-2]:
                    result.append(valley[:-1])
                    valley = valley[-2:]
                    is_descending = True

    if len(valley) > 2:
        result.append(valley)

    return result


def _trap_water(valley: List[int]) -> int:
    if len(valley) < 2:
        return 0

    valley_height = min(valley[0], valley[-1])
    count = 0

    for index in range(1, len(valley) - 1):
        count += valley_height - valley[index]

    return count
