package com.company;

import java.util.Scanner;
public class RedBlackTree {

    private final int BLUE = 0;
    private final int BLACK = 1;

    private class Node { //конструктор

        int key = -1, color = BLACK;
        Node left = nil, right = nil, parent = nil;

        Node(int key) {
            this.key = key;
        }
    }

    private final Node nil = new Node(-1);
    private Node root = nil;

    public void printTree(Node node) { //напечать дерево
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(((node.color==BLUE)?"Цвет: синий ":"Цвет: чёрный ")+"Ключ: "+node.key+" Родитель: "+node.parent.key+"\n");
        printTree(node.right);
    }

    private Node findNode(Node findNode, Node node) { //Найти элемент
        if (root == nil) {
            return null;
        }

        if (findNode.key < node.key) {
            if (node.left != nil) {
                return findNode(findNode, node.left);
            }
        } else if (findNode.key > node.key) {
            if (node.right != nil) {
                return findNode(findNode, node.right);
            }
        } else if (findNode.key == node.key) {
            return node;
        }
        return null;
    }

    private void insert(Node node) { //Добавить
        Node temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = BLUE;
            while (true) {
                if (node.key < temp.key) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key >= temp.key) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node); //перебалансировка после вставки
        }
    }

    private void fixTree(Node node) { //перебалансировка после вставки
        while (node.parent.color == BLUE) {
            Node uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == BLUE) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = BLUE;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = BLUE;
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                if (uncle != nil && uncle.color == BLUE) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = BLUE;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    //Double rotation needed
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = BLUE;
                rotateLeft(node.parent.parent);
            }
        }
        root.color = BLACK;
    }

    void rotateLeft(Node node) { //поворот влево
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {//Need to rotate root
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    void rotateRight(Node node) { //поворот вправо
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }


    void deleteTree(){//удалить дерево
        root = nil;
    }


    void transplant(Node target, Node with){ // поменять
        if(target.parent == nil){
            root = with;
        }else if(target == target.parent.left){
            target.parent.left = with;
        }else
            target.parent.right = with;
        with.parent = target.parent;
    }

    boolean delete(Node z){//удалить элемент
        if((z = findNode(z, root))==null)return false;
        Node x;
        Node y = z;
        int y_original_color = y.color;

        if(z.left == nil){
            x = z.right;
            transplant(z, z.right);
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left);
        }else{
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(y_original_color==BLACK)
            deleteFixup(x);
        return true;
    }

    void deleteFixup(Node x){ //перебалансировка дерева после удаления
        while(x!=root && x.color == BLACK){
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == BLUE){
                    w.color = BLACK;
                    x.parent.color = BLUE;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = BLUE;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = BLUE;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == BLUE){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == BLUE){
                    w.color = BLACK;
                    x.parent.color = BLUE;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = BLUE;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = BLUE;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == BLUE){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }
    Node treeMinimum(Node subTreeRoot){ //минимум
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }
    public void consoleUI() { //интерфейс
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("\n1.- Добавить элементы\n"
                    + "2.- Удалить элементы\n"
                    + "3.- Найти элемент\n"
                    + "4.- Напечатать дерево\n"
                    + "5.- Удалить дерево\n"
                    + "6. - Нарисовать дерево\n");
            int choice = scan.nextInt();
            int item;
            Node node = null;
            switch (choice) {
                case 1:
                    item = scan.nextInt();
                    while (item != -100) {
                        node = new Node(item);
                        insert(node);
                        item = scan.nextInt();
                    }
                    printTree(root);
                    break;
                case 2:
                    item = scan.nextInt();
                    while (item != -100) {
                        node = new Node(item);
                        System.out.print("\nУдаляем элемент" + item);
                        if (delete(node)) {
                            System.out.print(": Удалён");
                        } else {
                            System.out.print(": не существует");
                        }
                        item = scan.nextInt();
                    }
                    System.out.println();
                    printTree(root);
                    break;
                case 3:
                    item = scan.nextInt();
                    while (item != -100) {
                        node = new Node(item);
                        System.out.println((findNode(node, root) != null) ? "найден" : "не найден");
                        item = scan.nextInt();
                    }
                    break;
                case 4:
                    printTree(root);
                    break;
                case 5:
                    deleteTree();
                    System.out.println("Дерево удалено");
                    break;
                case 6:
                    System.out.println("Верхушка дерева слева.\nБратья, сёстры - на одном уровне, правый ребёнок(меньший) выше, левый ребёнок(больший) ниже по столбцам.\nСами дети выше родителя \nВ скобках: Красный - 0, чёрный - 1");
                    print_tree(root,0);

            }
        }
    }

    void print_tree(Node node,int l) { //нарисовать дерево

        if (node != nil) {
            if (node.left!=nil) print_tree(node.left, l+4);
            if(node==root) System.out.println("-------------------------->");
            if (node.right!=nil) print_tree(node.right, l + 4);
            if (l>0) {
                setwL(" ",l);
                System.out.print("   ");
            }
            System.out.println(node.key +"(" + node.color +")");
        }
    }

    public void setwL(String str, int width) //аналог setw из с++
    {
        for (int x = str.length(); x < width; ++x)
        {
            System.out.print(' ');
        }
        System.out.print(str);
    }

}