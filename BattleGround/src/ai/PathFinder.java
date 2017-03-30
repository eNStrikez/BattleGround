package ai;

import game.Block;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PathFinder {
	Block[][] map;
	int mapX;
	int mapY;

	/**
	 * @param m
	 * @param mX
	 * @param mY
	 */
	public PathFinder(Block[][] m, int mX, int mY) {
		map = m;
		mapX = mX;
		mapY = mY;
	}

	/**
	 * Searches for and returns the shortest possible path (if it exists)
	 * between the start and target
	 *
	 * @param start
	 * @param target
	 * @return
	 */
	public LinkedList<Block> findPath(Block target, Block start) {
		LinkedList<Block> closed = new LinkedList<Block>();
		LinkedList<Block> open = new LinkedList<Block>();

		// Returns null if the target is collidable, as entities can not move
		// into collidable blocks
		if (target.isCollidable()) {
			return null;
		}

		// Removes any precursors the blocks may have
		for (Block[] bRow : map) {
			for (Block b : bRow) {
				b.setPrecursor(null);
			}
		}

		// Sets the start cost as 0
		start.setG(0);
		// Sets the heuristic value of the start block as the heuristic distance
		// between the start block and the target block
		start.setH(findHeuristic(start, target));
		// Adds open to start
		open.add(start);
		// Sets the current block as the start block
		Block current = start;
		// Sets the count of steps as zero
		int count = 0;
		// Loops while open is not empty
		while (open.size() > 0) {
			// Sets the current block as the first block in open
			current = open.getFirst();
			// Loops through open to find the block with the lowest f value
			for (Block b : open) {
				if (b.getF() < current.getF()) {
					current = b;
				}
			}
			// Removes the current block from open
			open.remove(current);
			// If the current block is the target block, the path is complete
			// and the loop is broken
			if (current == target)
				break;
			// The successors for the current block are retrieved
			ArrayList<Block> n = getNeighbours(current);
			// The successors are looped through
			for (Block successor : n) {
				// The successor's cost is set as current's g value added to the
				// distance between the successor and current
				double successorCost = current.getG() + findDistance(successor, current);
				// If the successor is collidable, it is skipped
				if (successor.isCollidable()) {
					continue;
				}
				// If the successor is contained within open and the successor's
				// cost is greater than its g value, it is skipped
				if (open.contains(successor)) {
					if (successor.getG() <= successorCost) {
						continue;
					}
				}
				// If the successor is contained within closed and the
				// successor's cost is greater than its g value, it is skipped,
				// but if the successor's cost is lower than its g value, it is
				// removed from closed and not skipped
				else if (closed.contains(successor)) {
					if (successor.getG() <= successorCost) {
						continue;
					} else {
						closed.remove(successor);
					}
				} else {
					// If the successor is not within any other list, it's
					// heuristic distance to the target is calculated
					successor.setH(findHeuristic(successor, target) * (1 + 1 / 1000));
				}
				// The successor's g value is set as the cost
				successor.setG(successorCost);
				// The successor's precursor in the path is set as the current
				// block
				successor.setPrecursor(current);
				// The successor is added to open
				open.add(successor);
				// The count of steps is incremented
				count++;
			}
			// Adds the current block to the closed list
			closed.add(current);
		}
		// Initialises the path
		LinkedList<Block> path = new LinkedList<Block>();
		// Adds the current block, which is the target block, to the path
		path.add(current);
		// Sets the path length as 0
		int pathLength = 0;
		// Loops while the current block has a precursor
		while (current.getPrecursor() != null) {
			// Sets the current block as the current block's precursor
			current = current.getPrecursor();
			// Adds the current block to the path
			path.add(current);
			// Increments the path length
			pathLength++;
			// If the path length exceeds the count (rarely the case), the loop
			// is broken, as it is likely that the path has encountered an
			// unexpected error, usually being a infinite cycle between blocks
			if (count < pathLength) {
				break;
			}
		}
		// Returns the full path
		return path;
	}

	/**
	 * Returns the heuristic Manhattan distance between two blocks on the map
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public double findHeuristic(Block b1, Block b2) {
		return Math.random() + Math.abs(b1.getX() - b2.getX()) + Math.abs(b1.getY() - b2.getY());
	}

	/**
	 * Returns all the existing neighbours of the input block
	 *
	 * @param b
	 * @return
	 */
	public ArrayList<Block> getNeighbours(Block b) {
		ArrayList<Block> n = new ArrayList<Block>();
		// Checks if there is a block to the right of the input block
		if (b.getX() < mapX - 1) {
			// Adds the block to the right to the list of neighbours
			n.add(map[b.getX() + 1][b.getY()]);
		}
		// Checks if there is a block above the input block
		if (b.getY() < mapY - 1) {
			// Adds the block above the list of neighbours
			n.add(map[b.getX()][b.getY() + 1]);
		}
		// Checks if there is a block to the left of the input block
		if (b.getX() > 0) {
			// Adds the block to the left to the list of neighbours
			n.add(map[b.getX() - 1][b.getY()]);
		}
		// Checks if there is a block below the input block
		if (b.getY() > 0) {
			// Adds the block below to the list of neighbours
			n.add(map[b.getX()][b.getY() - 1]);
		}
		// Returns the list of neighbours
		return n;
	}

	/**
	 * Returns whether the input coordinates are valid for the current map
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean getValid(int x, int y) {
		boolean isValid = true;
		// Checks if the input coordinates are on the map
		if (x > mapX - 1 || x < 0) {
			isValid = false;
		} else if (y > mapY - 1 || y < 0) {
			isValid = false;
		}
		// Returns whether the input coordinates are on the map
		return isValid;
	}

	/**
	 * Returns the average movement cost between two adjacent blocks 
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public double findDistance(Block b1, Block b2) {
		return (b1.getCost() + b2.getCost() / 2);
	}

}
