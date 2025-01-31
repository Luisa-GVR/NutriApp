package com.prueba.demo.principal;

import com.prueba.demo.model.Food;
import com.prueba.demo.service.ConsumoAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Principal extends JFrame {

    // Consumo de API
    private ConsumoAPI consumoAPI = new ConsumoAPI();

    // Componentes Swing
    private JTextField queryField;
    private JTextArea resultArea;
    private JButton searchButton;

    public Principal() {
        // Configuración del JFrame
        setTitle("Buscar Alimentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Crear los componentes
        queryField = new JTextField(20); // Campo de texto para buscar alimentos
        resultArea = new JTextArea(10, 40); // Área de texto para mostrar resultados
        resultArea.setEditable(false); // No editable
        searchButton = new JButton("Buscar");

        // Panel para los componentes
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Introduce el nombre del alimento:"));
        panel.add(queryField);
        panel.add(searchButton);

        // Scroll para el área de texto
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Añadir los componentes al JFrame
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Evento del botón de búsqueda
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarAlimento(queryField.getText());
            }
        });
    }

    // Método para buscar alimento y mostrar los resultados en la interfaz
    public void buscarAlimento(String query) {
        Food alimento = consumoAPI.obtenerInformacionDeAlimentos(query); // Suponiendo que la API devuelve un único objeto Food

        // Limpiar el área de resultados
        resultArea.setText("");

        // Imprimir información del alimento obtenido
        if (alimento != null) {
            resultArea.append("Nombre: " + alimento.getFoodName() + "\n");
            resultArea.append("Calorías: " + alimento.getCalories() + "\n");
            resultArea.append("Proteínas: " + alimento.getProtein() + " g\n");
            resultArea.append("Grasas: " + alimento.getTotalFat() + " g\n");
            resultArea.append("Carbohidratos: " + alimento.getTotalCarbohydrate() + " g\n");
            resultArea.append("--------------------\n");
        } else {
            resultArea.append("No se encontraron alimentos con esa búsqueda.\n");
        }
    }

}
