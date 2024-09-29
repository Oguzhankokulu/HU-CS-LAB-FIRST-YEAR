import sys

def list_func(input):
    """Converts string lines to lists containing integers or strings
    as their elements and inserts the line lists in complete_list.
    """
    complete_list = []
    for line in input:
        # Create a list that contains strings in that line.
        line_split = line.split()
        for index in range(len(line_split)):
            # If the element is a number, then convert it to integer.
            try:
                line_split[index] = int(line_split[index])
            except ValueError:
                pass
        complete_list.append(line_split)
    return complete_list


def list_to_str_1(lines):
    """Converts list to a string."""
    line_str = ""
    for i in lines:
        line_str += i + " "
    # Excluding the last space by using list slicing.
    return line_str[:-1]


def list_to_str_2(list_input):
    """Converts a list of sublist to a complete string."""
    output_str = ""
    for line_list in list_input:
        # Converting the sublist to string line.
        line_str = list_to_str_1(line_list)
        output_str += line_str + "\n"
    # Extracting the last \n by using list slicing.
    return output_str[:-1]


def layout_creater(input_list):
    """Creates a layout based on the input list."""
    template = input_list[4:]
    layout = []
    for row in range(len(template)):
        # Append an empty row.
        layout.append([])
        for column in range(len(template[row])):
            # Append spaces as many as columns in current row.
            layout[row].append(" ")
    return layout


def row_counter(layout, row):
    """Counts occurrences of 'H' and 'B' in a given row of the layout."""
    h_counter = 0
    b_counter = 0
    column = 0
    while True:
        # If the column does not exist, break the loop.
        try:
            if layout[row][column] == "H":
                h_counter += 1
            elif layout[row][column] == "B":
                b_counter += 1
        except IndexError:
            break
        # Check the next column.
        column += 1
    return h_counter, b_counter


def column_counter(layout, column):
    """Counts occurrences of 'H' and 'B' in a given column of the layout."""
    h_counter = 0
    b_counter = 0
    row = 0
    while True:
        # If the row does not exist, break the loop.
        try:
            if layout[row][column] == "H":
                h_counter += 1
            elif layout[row][column] == "B":
                b_counter += 1
        except IndexError:
            break
        # Check the next row.
        row += 1
    return h_counter, b_counter


def count_checker(input_list, layout):
    """Checks whether the number of occurrences in the layout match the input values.
    If it matches with input values, returns False; If not, returns True.
    """
    for column in range(len(layout[0])):
        # High and base value inputs for that column.
        h_colmn = input_list[2][column]
        b_colmn = input_list[3][column]
        # Number of occurrences of 'H' and 'B' in that column.
        h_c_counter, b_c_counter = column_counter(layout, column)
        # If the input is -1 there is no limit, therefore no need to check.
        # If the input is not -1, checks if the number of occurrences in the layout match the input value.
        if h_colmn != -1 and h_colmn != h_c_counter:
            return True
        if b_colmn != -1 and b_colmn != b_c_counter:
            return True
    for row in range(len(layout)):
        # High and base value inputs for that row.
        h_row = input_list[0][row]
        b_row = input_list[1][row]
        # Number of occurrences of 'H' and 'B' in that row.
        h_r_counter, b_r_counter = row_counter(layout, row)
        # If the input is -1 there is no limit, therefore no need to check.
        # If the input is not -1, checks if the number of occurrences in the layout match the input value.
        if h_row != -1 and h_row != h_r_counter:
            return True
        if b_row != -1 and b_row != b_r_counter:
            return True

    return False


def neighbor_checker(row, column, layout):
    """Checks neighbors of a cell in the layout and excludes the other side of cell's tile."""
    # If the cell is located in the middle part of its column.
    if row > 0 and row < len(layout) - 1:
        column_neighbor = [layout[row - 1][column], layout[row + 1][column]]
    # If the cell is located in the last row of its column.
    elif row > 0:
        column_neighbor = [layout[row - 1][column]]
    # If the cell is located in the first row of its column.
    else:
        column_neighbor = [layout[row + 1][column]]

    # If the cell is located in the middle part of its row.
    if column > 0 and column < len(layout[0]) - 1:
        row_neighbor = [layout[row][column - 1], layout[row][column + 1]]
    # If the cell is located in the last column of its row.
    elif column > 0:
        row_neighbor = [layout[row][column - 1]]
    # If the cell is located in the first column of its row.
    else:
        row_neighbor = [layout[row][column + 1]]

    neighbors = column_neighbor + row_neighbor
    # Exclude the other side of the cell's tile.
    if layout[row][column] == "H":
        neighbors.remove("B")
    if layout[row][column] == "B":
        neighbors.remove("H")
    return neighbors


def tile_placer(input_list, layout, template, row, column):
    """Tries to solve the game by placing tiles according to the recursive backtracking approach.
    Places tiles on the layout based on the template.
    If the game is solved returns True.
    If the game cannot be solved returns False.
    """
    # Base case: if all values matches with input, the valley is created.
    if not count_checker(input_list, layout):
        return True

    for value in ["HB", "BH", "NN"]:
        # If the cell represents the left side of a tile. Placement is done for 'L R' couple.
        if template[row][column] == "L":
            # Checks if the first element of the value is not in the cell's neighbors or if it's 'N'.
            if value[0] not in neighbor_checker(row, column, layout) or value[0] == "N":
                # Checks if the second element of the value is not in the other cell of the tile's neighbors or
                # if it is 'N'.
                if value[1] not in neighbor_checker(row, column + 1, layout) or value[1] == "N":
                    # Place the tile on the layout.
                    layout[row][column] = value[0]
                    layout[row][column + 1] = value[1]

                    # Tries the recursive approach for the next tile in that row.
                    try:
                        if tile_placer(input_list, layout, template, row, column + 2):
                            return True

                    # If there is no next tile in that row.
                    except IndexError:
                        # Tries the recursive approach for the next row's first tile.
                        try:
                            if tile_placer(input_list, layout, template, row + 1, 0):
                                return True
                        # If there is no next row in the layout then backtracks.
                        except IndexError:
                            continue
                        # If there is no solution for this approach then backtracks.
                        continue

        # If the cell represents the upper side of a tile. Placement is done for 'U D' couple.
        elif template[row][column] == "U":
            # Checks if the first element of the value is not in the cell's neighbors or if it's 'N'.
            if value[0] not in neighbor_checker(row, column, layout) or value[0] == "N":
                # Checks if the second element of the value is not in the other cell of the tile's neighbors or
                # if it is 'N'.
                if value[1] not in neighbor_checker(row + 1, column, layout) or value[1] == "N":
                    # Places the tile on the layout.
                    layout[row][column] = value[0]
                    layout[row + 1][column] = value[1]

                    # Tries the recursive approach for the next tile in that row.
                    try:
                        if tile_placer(input_list, layout, template, row, column + 1):
                            return True

                    # If there is no next tile in that row.
                    except IndexError:
                        # Tries the recursive approach for the next row's first tile.
                        try:
                            if tile_placer(input_list, layout, template, row + 1, 0):
                                return True
                        # If there is no next row in the layout then backtracks and
                        # tries the next value if there is any untried left.
                        except IndexError:
                            continue
                        # If there is no solution for this approach then backtracks and
                        # tries the next value if there is any untried left.
                        continue

        # If the cell represents the right side of a tile. No placements are done in right side.
        elif template[row][column] == "R":
            # Tries the recursive approach for the next tile in that row.
            try:
                if tile_placer(input_list, layout, template, row, column + 1):
                    return True
                # If there is no solution for this approach then backtracks.
                return False

            # If there is no next tile in that row.
            except IndexError:
                # Tries the recursive approach for the next row's first tile.
                try:
                    if tile_placer(input_list, layout, template, row + 1, 0):
                        return True
                    # If there is no solution for this approach then backtracks.
                    return False
                # If there is no next row in the layout then backtracks.
                except IndexError:
                    return False

        # If the cell represents the down side of a tile. No placements are done in down side.
        elif template[row][column] == "D":
            # Tries the recursive approach for the next tile in that row.
            try:
                if tile_placer(input_list, layout, template, row, column + 1):
                    return True
                # If there is no solution for this approach then backtracks.
                return False

            # If there is no next tile in that row.
            except IndexError:
                # Tries the recursive approach for the next row's first tile.
                try:
                    if tile_placer(input_list, layout, template, row + 1, 0):
                        return True
                    # If there is no solution for this approach then backtracks.
                    return False
                # If there is no next row in the layout then backtracks.
                except IndexError:
                    return False

    # If there is no solution, returns False
    return False


def main():
    """Main function that reads input, processes it and writes the output."""
    input_file = open(sys.argv[1], "r")
    output_file = open(sys.argv[2], "w")
    input_list = list_func(input_file)
    # Table template in the input.
    template = input_list[4:]
    # Layout according to the input.
    layout = layout_creater(input_list)
    # Starting row and column.
    row = 0
    column = 0
    # If there is a solution, writes the solution to the output.
    if tile_placer(input_list, layout, template, row, column):
        layout_str = list_to_str_2(layout)
        output_file.write(layout_str)

    # If there is no solution, writes 'No solution!' to the output.
    else:
        output_file.write("No solution!")


if __name__ == "__main__":
    main()
