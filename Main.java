
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
    private Font font;
    private JPanel panelArbol;
    private JTextField texto;
    private RBT<Integer> rbt = new RBT<>();
    // Mapa de posiciones como atributo
    private Map<Nodo<Integer>, Point> posiciones = new HashMap<>();
    
    public Main() {
        super("graficar RBT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(Color.BLUE);
        setLocationRelativeTo(null); // Centrar
        font = new Font("Arial", Font.BOLD, 50);
        setFont(font);

        //Panel para graficar el arbol
        panelArbol = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarArbol((Graphics2D) g);
            }
        };
        panelArbol.setBackground(Color.WHITE);
        this.add(panelArbol,BorderLayout.CENTER);

        //Botones de insertar y area de texto
        JPanel panel = new JPanel(); // por defecto FlowLayout
        panel.setLayout(new GridLayout(1,3));
        this.add(panel,BorderLayout.NORTH);
        JButton boton = new JButton("Insertar");
        boton.setFont(font);
        JButton botonBorrar = new JButton("Eliminar");
        botonBorrar.setFont(font);
        boton.addActionListener(e -> this.dibujar()); 
        botonBorrar.addActionListener(e -> this.eliminar());        
        texto = new JTextField(40);
        texto.setFont(font);
        texto.setHorizontalAlignment(JTextField.CENTER);
        panel.add(texto);
        panel.add(boton);
        
        panel.add(botonBorrar);
        setVisible(true);
    }

    public void calcularPosiciones(Nodo<Integer> nodo, int nivel, Map<Nodo<Integer>, Point> posiciones, int ancho, int alto, int x, int aa) {
        if (nodo == null || nodo.elemento == null) return;
        int distancia = (ancho / (nivel+1)) / 3; //espacio entre nodos
        //System.out.println("Nivel: " + nivel + " Distancia: " + distancia+ " x: " + x);
        int y =  (alto / aa * nivel)-50; // Espaciado vertical
        posiciones.put(nodo, new Point(x, y));
        calcularPosiciones(nodo.left, nivel + 1, posiciones, ancho, alto, x-distancia, aa);
        calcularPosiciones(nodo.right, nivel + 1, posiciones, ancho, alto, x+distancia, aa);
    }


    public void dibujar() {
        
        // 1) Insertar en el árbol
        int value = Integer.parseInt(texto.getText());
        rbt.insert(value);

        // 2) Calcular 'aa' (altura aprox del árbol)
        int aa = (int) Math.ceil(Math.log(rbt.size) / Math.log(2));
        aa = Math.max(aa, 1) + 2;

        // 3) Recalcular posiciones usando el tamaño real del panel
        int ancho = panelArbol.getWidth();
        int alto = panelArbol.getHeight();
        if (ancho <= 0 || alto <= 0) {
            // Por si se llama antes de que el panel tenga tamaño
            ancho = 1000;
            alto = 600;
        }

        posiciones.clear();;
        int xCentro = ancho / 2;
        calcularPosiciones(rbt.root, 1, posiciones, ancho, alto, xCentro, aa);

        // System.out.println("Tamaño del árbol: " + rbt.size);
        // for (Map.Entry<Nodo<Integer>, Point> e : posiciones.entrySet()) {
        //     Nodo<Integer> n = e.getKey();
        //     Point p = e.getValue();
        //     System.out.println("Nodo: " + n.elemento + " Posición: (" + p.x + ", " + p.y + ")");
        // }

        // 4) Repintar el panel
        panelArbol.repaint();        
    }

    public Nodo<Integer> buscarNodo(Nodo<Integer> actual, int elemento) {
        if (actual == null || actual == rbt.vacio) return null;

        if (elemento == actual.elemento) return actual;

        if (elemento < actual.elemento)
            return buscarNodo(actual.left, elemento);
        else
            return buscarNodo(actual.right, elemento);
    }


    public void eliminar() {
        if (texto.getText().isEmpty()) return;
        int value = Integer.parseInt(texto.getText());
        Nodo<Integer> nodo = buscarNodo(rbt.root, value);

        if (nodo == null || nodo == rbt.vacio) {
            return;
        }
        rbt.delete(nodo);
        rbt.size--; 

        int aa = (int) Math.ceil(Math.log(Math.max(rbt.size,1)) / Math.log(2));
        aa = Math.max(aa, 1) + 2;

        int ancho = panelArbol.getWidth();
        int alto = panelArbol.getHeight();
        if (ancho <= 0 || alto <= 0) {
            ancho = 1000;
            alto = 600;
        }
        posiciones.clear();
        int xCentro = ancho / 2;
        calcularPosiciones(rbt.root, 1, posiciones, ancho, alto, xCentro, aa);
        panelArbol.repaint();
    }


    
    private void dibujarArbol(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int diameter = 100;

        for (Map.Entry<Nodo<Integer>, Point> entry : posiciones.entrySet()) {
            Nodo<Integer> nodo = entry.getKey();
            Point p = entry.getValue();

            int x = p.x - diameter / 2;
            int y = p.y - diameter / 2;

            // Color del nodo
            g2.setColor(nodo.color); // asegúrate que nunca sea null
            g2.fillOval(x, y, diameter, diameter);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, diameter, diameter);

            g2.setFont(font);
            String text = "" + nodo.elemento;
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (diameter - fm.stringWidth(text)) / 2;
            int textY = y + ((diameter - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, textX, textY);
        }
    }

public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main();
            });
    }
}