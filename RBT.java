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

    public void transplant(Nodo<T> u, Nodo<T> v){
        if(u.parent==vacio){
            root=v;
        }else if(u==u.parent.left){
            u.parent.left=v;
        }else{
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    public void delete(Nodo<T> z){
        Nodo<T> y =z;
        Nodo<T> x;
        Color yColor = y.color;
        if(z.left==vacio){
            x = z.right;
            transplant(z, z.right);
        }else if(z.right==vacio){
            x=z.left;
            transplant(z, z.left);
        }else{
            y = minimoRecursivo(z.right);
            yColor = y.color;
            x=y.right;

            if(y.parent==z){
                x.parent=y;
            }else{
                transplant(y, y.right);
                y.right=z.right;
                y.right.parent=y;
            }
            transplant(z, y);
            y.left=z.left;
            y.left.parent=y;
            y.color=z.color;
        }if(yColor==Color.GRAY){
            deleteFixup(x);
        }
    }

    public void deleteFixup(Nodo<T> x){
        Nodo<T> w;
        while(x!=root && x.color==Color.GRAY){
            if(x==x.parent.left){
                w=x.parent.right;
                if(w.color==Color.RED){
                    w.color=Color.GRAY;
                    x.parent.color=Color.RED;
                    rotateLeft(x.parent);
                    w=x.parent.right;
                }
                if(w.left.color==Color.GRAY && w.right.color==Color.GRAY){
                    w.color=Color.RED;
                    x=x.parent;
                }else if(w.right.color==Color.GRAY){
                    w.left.color=Color.GRAY;
                    w.color=Color.RED;
                    rotateRight(w);
                    w=x.parent.right;
                }
                w.color=x.parent.color;
                x.parent.color=Color.GRAY;
                w.right.color=Color.GRAY;
                rotateLeft(x.parent);
                x=root;
            }else{
                w=x.parent.left;
                if(w.color==Color.RED){
                    w.color=Color.GRAY;
                    x.parent.color=Color.RED;
                    rotateRight(x.parent);
                    w=x.parent.left;
                }
                if(w.right.color==Color.GRAY && w.left.color==Color.GRAY){
                    w.color=Color.RED;
                    x=x.parent;
                }else if(w.left.color==Color.GRAY){
                    w.right.color=Color.GRAY;
                    w.color=Color.RED;
                    rotateLeft(w);
                    w=x.parent.left;
                }
                w.color=x.parent.color;
                x.parent.color=Color.GRAY;
                w.left.color=Color.GRAY;
                rotateRight(x.parent);
                x=root;
            }
            x.color=Color.GRAY;
        }
    }

    public Nodo<T> getMinimo(){
        if(root==vacio){
            return vacio;
        }
        return minimoRecursivo(root);
    }
    public Nodo<T> minimoRecursivo(Nodo<T> nodo){
        if (nodo.left == vacio){
            return nodo;
        }
        return minimoRecursivo(nodo.left);
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
