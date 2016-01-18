package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stack {
	private List<Block> stack;
	Board board = new Board();
	
	public Stack() {
		stack = new ArrayList<Block>();
		Block block1 = new Block(Color.RED, Shape.CIRCLE);
		Block block2 = new Block(Color.RED, Shape.CLOVER);
		Block block3 = new Block(Color.RED, Shape.SQUARE);
		Block block4 = new Block(Color.RED, Shape.CROSS);
		Block block5 = new Block(Color.RED, Shape.DIAMOND);
		Block block6 = new Block(Color.RED, Shape.STAR);
		Block block7 = new Block(Color.BLUE, Shape.CIRCLE);
		Block block8 = new Block(Color.BLUE, Shape.CLOVER);
		Block block9 = new Block(Color.BLUE, Shape.SQUARE);
		Block block10 = new Block(Color.BLUE, Shape.CROSS);
		Block block11 = new Block(Color.BLUE, Shape.DIAMOND);
		Block block12 = new Block(Color.BLUE, Shape.STAR);
		Block block13 = new Block(Color.GREEN, Shape.CIRCLE);
		Block block14 = new Block(Color.GREEN, Shape.CLOVER);
		Block block15 = new Block(Color.GREEN, Shape.SQUARE);
		Block block16 = new Block(Color.GREEN, Shape.CROSS);
		Block block17 = new Block(Color.GREEN, Shape.DIAMOND);
		Block block18 = new Block(Color.GREEN, Shape.STAR);
		Block block19 = new Block(Color.PURPLE, Shape.CIRCLE);
		Block block20 = new Block(Color.PURPLE, Shape.CLOVER);
		Block block21 = new Block(Color.PURPLE, Shape.SQUARE);
		Block block22 = new Block(Color.PURPLE, Shape.CROSS);
		Block block23 = new Block(Color.PURPLE, Shape.DIAMOND);
		Block block24 = new Block(Color.PURPLE, Shape.STAR);
		Block block25 = new Block(Color.YELLOW, Shape.SQUARE);
		Block block26 = new Block(Color.YELLOW, Shape.CLOVER);
		Block block27 = new Block(Color.YELLOW, Shape.CIRCLE);
		Block block28 = new Block(Color.YELLOW, Shape.CROSS);
		Block block29 = new Block(Color.YELLOW, Shape.DIAMOND);
		Block block30 = new Block(Color.YELLOW, Shape.STAR);
		Block block31 = new Block(Color.ORANGE, Shape.CIRCLE);
		Block block32 = new Block(Color.ORANGE, Shape.SQUARE);
		Block block33 = new Block(Color.ORANGE, Shape.CLOVER);
		Block block34 = new Block(Color.ORANGE, Shape.CROSS);
		Block block35 = new Block(Color.ORANGE, Shape.DIAMOND);
		Block block36 = new Block(Color.ORANGE, Shape.STAR);
		for(int i = 0; i < 3; i++) {
			stack.add(block36);
			stack.add(block35);
			stack.add(block34);
			stack.add(block33);
			stack.add(block32);
			stack.add(block31);
			stack.add(block30);
			stack.add(block29);
			stack.add(block28);
			stack.add(block27);
			stack.add(block26);
			stack.add(block25);
			stack.add(block24);
			stack.add(block23);
			stack.add(block22);
			stack.add(block21);
			stack.add(block20);
			stack.add(block19);
			stack.add(block18);
			stack.add(block17);
			stack.add(block16);
			stack.add(block15);
			stack.add(block14);
			stack.add(block13);
			stack.add(block12);
			stack.add(block11);
			stack.add(block10);
			stack.add(block9);
			stack.add(block8);
			stack.add(block7);
			stack.add(block6);
			stack.add(block5);
			stack.add(block4);
			stack.add(block3);
			stack.add(block2);
			stack.add(block1);
		}
		shuffleStack();
	}
	
	public boolean isValidSwap(List<SwapMove> swaps) {
		boolean result = true;
		if (swaps.size() > stack.size()) {
			result = false;
		}
		return result;
	}
	
	public void shuffleStack() {
		Collections.shuffle(stack);
	}
	
	public void giveBack(List<Block> blocks) {
		for(int i = 0; i < blocks.size(); i++) {
			stack.add(blocks.get(i));
		}
	}
	
	public List<Block> give(int x) {
		List<Block> hand = new ArrayList<Block>();
		for(int i = 0; i < x; i++) {
			hand.add(stack.get(i));
			stack.remove(i);
		}
		return hand;
	}

}
