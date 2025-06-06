package es.uned.lsi.eped.DataStructures;

public class BSTree<E extends Comparable<E>> extends Tree<E> implements BSTreeIF<E> {

	private BSTree<E> leftChild;
	private BSTree<E> rightChild;
	private Order insertionOrder;

	public BSTree() {
		super();
		this.leftChild = null;
		this.rightChild = null;
		this.insertionOrder = Order.ASCENDING;
	}

	public BSTree(Order instOrder) {
		super();
		this.leftChild = null;
		this.rightChild = null;
		this.insertionOrder = instOrder;
	}

	/* Devuelve el hijo izquierdo del Arbol */
	public BSTree<E> getLeftChild() {
		return this.leftChild;
	}

	/* Devuelve el hijo derecho del Arbol */
	public BSTree<E> getRightChild() {
		return this.rightChild;
	}

	/* Modifica la raíz */
	private void setRoot(E e) {
		this.root = e;
	}

	/* Modifica el hijo izquierdo */
	private void setLeftChild(BSTree<E> child) {
		this.leftChild = child;
	}

	/* Modifica el hijo derecho */
	private void setRightChild(BSTree<E> child) {
		this.rightChild = child;
	}

	/*
	 * Metodo que devuelve el padre del nodo buscado.
	 **/
	private BSTree<E> searchParent(BSTree<E> node, BSTree<E> root) {
		if (root == null) {
			return null;
		} else {
			if (node.getRoot().compareTo(root.getRoot()) == 0) {
				return null;
			} else if (placeOnTheLeft(node, root)) {
				BSTree<E> child = searchParent(node, root.getLeftChild());
				if (child == null) {
					return root;
				}
				return child;
			} else {
				BSTree<E> child = searchParent(node, root.getRightChild());
				if (child == null) {
					return root;
				}
				return child;
			}
		}
	}

	private boolean placeOnTheLeft(BSTreeIF<E> node, BSTreeIF<E> parent) {
		return (this.getOrder().equals(Order.ASCENDING) && node.getRoot().compareTo(parent.getRoot()) < 0)
				|| (this.getOrder().equals(Order.DESCENDING) && node.getRoot().compareTo(parent.getRoot()) > 0);
	}
	
	public void add(E newValue) {

		if (this.getRoot() == null) {
			this.setRoot(newValue);

		} else {
			BSTree<E> newNode = new BSTree<>(this.getOrder());
			newNode.setRoot(newValue);
			BSTree<E> parent = searchParent(newNode, this);

			if (parent == null) {
				this.setRoot(newValue);
			} else if (placeOnTheLeft(newNode, parent)) {
				if(parent.getLeftChild()==null)
				{
					parent.setLeftChild(newNode);
				}
			} else {
				if(parent.getRightChild()==null)
				{
					parent.setRightChild(newNode);
				}
			}
		}
	}

	public void remove(E newValue) {
		if (this.getRoot() != null) {
			
			if (this.getRoot().compareTo(newValue) == 0 && this.getNumChildren() == 0) {
				this.setRoot(null);
			} else {
				
				BSTree<E> newNode = new BSTree<>(this.getOrder());
				newNode.setRoot(newValue);
				BSTree<E> parent = searchParent(newNode, this);
				if(parent==null)
				{
					if (this.getLeftChild() == null)
					{
						this.setRoot(this.getRightChild().getRoot());						
						this.setLeftChild(this.getRightChild().getLeftChild());
						this.setRightChild(this.getRightChild().getRightChild());
					}
					else if(this.getRightChild()==null)
					{
						this.setRoot(this.getLeftChild().getRoot());						
						this.setRightChild(this.getLeftChild().getRightChild());
						this.setLeftChild(this.getLeftChild().getLeftChild());
					}
					else
					{
						this.setRightChild(replaceNode(this, this.getRightChild()));
					}					
				}
				else
				{
					BSTree<E> child = null;
					if (placeOnTheLeft(newNode, parent)) 
					{
						child = parent.getLeftChild();
						if (child.getLeftChild() == null)
						{
							parent.setLeftChild(child.getRightChild());
						}
						else if(child.getRightChild()==null)
						{
							parent.setLeftChild(child.getLeftChild());
						}
						else
						{
							child.setRightChild(replaceNode(child, child.getRightChild()));
						}	
					} 
					else {
						child = parent.getRightChild();
						if (child.getLeftChild() == null)
						{
							parent.setRightChild(child.getRightChild());
						}
						else if(child.getRightChild()==null)
						{
							parent.setRightChild(child.getLeftChild());
						}
						else
						{
							child.setRightChild(replaceNode(child, child.getRightChild()));
						}
					}
				}				
			}
		}
	}
	
	/**
	 * Metodo que reemplaza el valor de uno nodo a eliminar con dos hijos por su nodo sucesor.
	 * */
	private BSTree<E> replaceNode(BSTree<E> nodeToRemove, BSTree<E> succesor) {
		if (succesor.getLeftChild() == null) {
			nodeToRemove.setRoot(succesor.getRoot());
			return succesor.getRightChild();
		} else {
			succesor.setLeftChild(replaceNode(nodeToRemove, succesor.getLeftChild()));
			return succesor;
		}
	}

	public Order getOrder() {
		return this.insertionOrder;
	}

	public int getNumChildren() {
		int nC = 0;
		if (this.leftChild != null) {
			nC++;
		}
		if (this.rightChild != null) {
			nC++;
		}
		return nC;
	}

	public int getFanOut() {
		if (getNumChildren() == 2) {
			return 2;
		}
		if (this.leftChild != null) {
			return Math.max(1, this.leftChild.getFanOut());
		}
		if (this.rightChild != null) {
			return Math.max(1, this.rightChild.getFanOut());
		}
		return 0;
	}

	/* Devuelve la altura del árbol */
	public int getHeight() {
		if ( isEmpty() ) { return -1; }
		int hLC = -1;
		if ( this.leftChild != null ) { hLC = this.leftChild.getHeight(); }
		int hRC = -1;
		if ( this.rightChild != null ) { hRC = this.rightChild.getHeight(); }
		return 1 + ((hLC > hRC)?hLC:hRC);
	}

	/* Reimplementación/Especialización de algunos métodos de Collection */
	
	/* Devuelve el número de nodos del árbol */
	public int size() {
		if ( isEmpty() ) { return 0; }
		int s = 1;
		if ( this.leftChild != null ) { s = s + this.leftChild.size(); }
		if ( this.rightChild != null ) { s = s + this.rightChild.size(); }
		return s;
	}

	/* Nos dice si el árbol binario de búsqueda está vacío o no */
	public boolean isEmpty() {
		return (this.getRoot() == null && this.getRightChild() == null && this.getLeftChild() == null);
	}
	
	/* Vacía el árbol binario */
	public void clear() {
		super.clear();
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public boolean contains(E newValue) {
		if (this.getRoot() == null) {
			return false;
		}

		BSTree<E> newNode = new BSTree<>(this.getOrder());
		newNode.setRoot(newValue);
		BSTree<E> parent = searchParent(newNode, this);

		if (parent == null) {
			return true;
		} else {
			if (placeOnTheLeft(newNode, parent)) {
				return parent.getLeftChild() != null;
			} else {
				return parent.getRightChild() != null;
			}
		}
	}

	
	public IteratorIF<E> iterator(Object mode) {
		if (mode.equals(IteratorModes.DIRECTORDER)) {
			QueueIF<E> queue = new Queue<E>();
			inorder(this, queue);
			return queue.iterator();
		} else if (mode.equals(IteratorModes.REVERSEORDER)) {
			StackIF<E> stack = new Stack<E>();
			inorder(this, stack);
			return stack.iterator();
		}
		return null;
	}
	
	/* Recorre el árbol en inorden */
	private void inorder(BSTree<E> t, QueueIF<E> q) {
		if ( !t.isEmpty() ) {
			if ( t.getLeftChild() != null ) { inorder(t.getLeftChild(),q); }
			q.enqueue(t.getRoot());
			if ( t.getRightChild() != null ) { inorder(t.getRightChild(),q); }
		}
	}
	
	/* Recorre el árbol en inorden */
	private void inorder(BSTree<E> t, StackIF<E> stack) {
		if ( !t.isEmpty() ) {
			if ( t.getLeftChild() != null ) { inorder(t.getLeftChild(),stack); }
			stack.push(t.getRoot());
			if ( t.getRightChild() != null ) { inorder(t.getRightChild(),stack); }
		}
	}
	
	public String toString() {
		return toString_aux(0,0);   
    }
	
	/**MIOS NO EN EL OFICIAL*/

	private String toString_aux(int nivel, int numHijo){
	    StringBuilder buff = new StringBuilder();
	    if (nivel==0){
	    	buff.append("BSTree:");
		    buff.append("\n");
		    buff.append("Ra�z -> " + getRoot());
	    }
	    else{
		    buff.append("\n");
		    for (int i=1;i<=nivel;i++) buff.append("\t");
		    if(numHijo==1){
			    buff.append("Hijo izq."+" -> " + getRoot());
		    }
		    else if(numHijo==2){
			    buff.append("Hijo der."+" -> " + getRoot());
		    }
	    }
	    if(!(leftChild==null)){
	    	String saux = this.leftChild.toString_aux(nivel+1,1);
	    	buff.append(saux);
	    }
	    
	    if(!(rightChild==null)){
	    	String saux = this.rightChild.toString_aux(nivel+1,2);
	    	buff.append(saux);   
	    }
	    return buff.toString();
    }

	
	/* Recorre el arbol en preorden */	
	public IteratorIF<E> preOrden(BSTree<E> t, Queue<E> q) {
		if ( !t.isEmpty() ) {
			q.enqueue(t.getRoot());
			if ( t.getLeftChild() != null ) { preOrden(t.getLeftChild(),q); }			
			if ( t.getRightChild() != null ) { preOrden(t.getRightChild(),q); }
			return q.iterator();
		}
		return null;
	}
	
	/* Recorre el arbol en postorden */	
	public IteratorIF<E> postOrden(BSTree<E> t, Queue<E> q) {
		if ( !t.isEmpty() ) {			
			if ( t.getLeftChild() != null ) { postOrden(t.getLeftChild(),q); }			
			if ( t.getRightChild() != null ) { postOrden(t.getRightChild(),q); }
			q.enqueue(t.getRoot());
			return q.iterator();
		}
		return null;
	}
	
	public List<E> boundaries()
	{
		IteratorIF<E> it = boundaries(this, new Queue<>());
		List<E> l = new List<E>();
		int pos = 1;
		while(it.hasNext())
		{
			l.insert(pos, it.getNext());
			pos++;
		}
		return l;
	}
	
	
	/* Recorre el arbol en preorden y solo almacena si es hoja*/	
	private IteratorIF<E> boundaries(BSTree<E> t, Queue<E> q) {
		if ( !t.isEmpty() ) {
			if(t.getLeftChild()==null && t.getRightChild()==null) {q.enqueue(t.getRoot());}
			if ( t.getLeftChild() != null ) { boundaries(t.getLeftChild(),q); }			
			if ( t.getRightChild() != null ) { boundaries(t.getRightChild(),q); }
			return q.iterator();
		}
		return null;
	}
	


}
