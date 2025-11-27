import java.awt.Color;

public class RBT<T extends Comparable<T>> {
    public Nodo<T> root;
    public int size;
    public Nodo<T> vacio;

    public RBT() {
        this.vacio = new Nodo<>(null);
        this.vacio.color = Color.GRAY;
        this.vacio.left = this.vacio;
        this.vacio.right = this.vacio;
        this.vacio.parent = this.vacio;
        this.root = vacio;
        this.size = 0;
    }

    public void insert(T elemento) {
        Nodo<T> z = new Nodo<>(elemento);
        z.left = vacio;
        z.right = vacio;
        z.parent = vacio;
        z.color = Color.RED;
        size++;

        Nodo<T> y = vacio;
        Nodo<T> x = root;

        while (x != vacio) {
            y = x;
            if (z.elemento.compareTo(x.elemento) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        if (y == vacio) {
            root = z; 
        } else if (z.elemento.compareTo(y.elemento) < 0) {
            y.left = z;
        } else {
            y.right = z;
        }
        
        insertFixup(z);
        root.color = Color.GRAY;
    }

    public void rotateLeft(Nodo<T> x) {
        Nodo<T> y = x.right;
        x.right = y.left;

        if (y.left != vacio) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == vacio) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    public void rotateRight(Nodo<T> y) {
        Nodo<T> x = y.left;
        y.left = x.right;

        if (x.right != vacio) {
            x.right.parent = y;
        }

        x.parent = y.parent;

        if (y.parent == vacio) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        x.right = y;
        y.parent = x;
    }

    public void insertFixup(Nodo<T> z) {
        while (z.parent.color == Color.RED) {
            if (z.parent == z.parent.parent.left) {
                Nodo<T> y = z.parent.parent.right;
                if (y.color == Color.RED) {
                    z.parent.color = Color.GRAY;
                    y.color = Color.GRAY;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = Color.GRAY;
                    z.parent.parent.color = Color.RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Nodo<T> y = z.parent.parent.left;
                if (y.color == Color.RED) {
                    z.parent.color = Color.GRAY;
                    y.color = Color.GRAY;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = Color.GRAY;
                    z.parent.parent.color = Color.RED;
                    rotateLeft(z.parent.parent);
                }
            }

            if (z == root) {
                break;
            }
        }
        root.color = Color.GRAY;
    }

    public int blackHeight(Nodo<T> x) {
        if (x == vacio) {
            return 1;
        }
        int bhL = blackHeight(x.left);
        int bhR = blackHeight(x.right);
        if (bhL == 0 || bhL != bhR) {
            return 0;
        }
        if(x.color == Color.GRAY){
            return bhL+1;
        }else{
            return bhL;
        }
    }

    public void inOrder() {
        if (root == vacio) {
            System.out.println("El árbol está vacío");
            return;
        }
        inOrderRecursivo(root);
    }

    public void inOrderRecursivo(Nodo<T> nodo) {
        if (nodo == vacio) return;

        inOrderRecursivo(nodo.left);
        System.out.println(nodo.elemento);
        inOrderRecursivo(nodo.right);
    }
}
