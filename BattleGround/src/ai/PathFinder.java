package ai;

import game.Block;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PathFinder {
	Block[][] map;
	ArrayList<Block> scanned = new ArrayList<Block>();
	int mapX;
	int mapY;

	public PathFinder(Block[][] m, int mX, int mY) {
		map = m;
		mapX = mX;
		mapY = mY;
	}

	public LinkedList<Block> findPath(Block start, Block target) {
		LinkedList<Block> closed = new LinkedList<Block>();
		LinkedList<Block> open = new LinkedList<Block>();

		scanned.clear();
		start.setG(0);
		start.setH(findHeuristic(start, target)* (1+1/1000));
		open.add(start);
		Block current = start;

		while (open.size() > 0) {
			current = open.getFirst();
			for(Block b: open){
				if(b.getF() < current.getF()){
					current = b;
				}
			}
			open.remove(current);

			if (current == target)
				break;
			ArrayList<Block> n = getNeighbours(current);
			for (Block successor : n) {
				double successorCost = current.getG() + findDistance(successor, current);
				if (open.contains(successor)) {
					if (successor.getG() <= successorCost) {
						continue;
					}
				} else if (closed.contains(successor)) {
					if (successor.getG() <= successorCost) {
						continue;
					} else {
						closed.remove(successor);
						open.add(successor);
					}
				} else {
					open.add(successor);
					successor.setH(findHeuristic(successor, target) * (1+1/1000));
				}
				successor.setG(successorCost);
				successor.setPrecursor(current);

			}

			closed.add(current);
		}

		LinkedList<Block> path = new LinkedList<Block>();
		path.add(current);
		while (current.getPrecursor() != null) {
			current = current.getPrecursor();
			path.add(current);
		}
		return path;
	}

	public double findHeuristic(Block b1, Block b2) {
		return 1 + Math.abs(b1.getX() - b2.getX()) + Math.abs(b1.getY() - b2.getY());
	}

	public ArrayList<Block> getNeighbours(Block b) {
		ArrayList<Block> n = new ArrayList<Block>();

		if (b.getX() < mapX - 1) {
			n.add(map[b.getX() + 1][b.getY()]);
		}
		if (b.getY() < mapY - 1) {
			n.add(map[b.getX()][b.getY() + 1]);
		}
		if (b.getX() > 0) {
			n.add(map[b.getX() - 1][b.getY()]);
		}
		if (b.getY() > 0) {
			n.add(map[b.getX()][b.getY() - 1]);
		}

		return n;
	}

	public boolean getValid(int x, int y) {
		boolean isValid = true;
		if (x > mapX - 1 || x < 0) {
			isValid = false;
		} else if (y > mapY - 1 || y < 0) {
			isValid = false;
		}

		return isValid;
	}

	public double findDistance(Block b1, Block b2) {
		return (b1.getCost() + b2.getCost() / 2);
	}

}
