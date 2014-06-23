package gameoflife;

import java.awt.Graphics2D;

/**
 *
 * @author Cypher
 */
public class Board
{

    private Node[][] nodeList;
    private int size, xSize, ySize;

    public Board(int xSize, int ySize)
    {
        this.xSize = xSize;
        this.ySize = ySize;
        nodeList = new Node[this.xSize][this.ySize];

        for (int i = 0; i < this.ySize; i++)
        {
            for (int j = 0; j < this.xSize; j++)
            {
                nodeList[j][i] = new Node(false);
            }
        }
    }

    public int getSize()
    {
        return this.nodeList.length;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.ySize; i++)
        {
            for (int j = 0; j < this.xSize; j++)
            {
                result.append(nodeList[j][i]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public int getNeighbourCount(int x, int y)
    {
        int result = 0;
        
        int[] xCoords = {x, x - 1, x + 1};
        int[] yCoords = {y, y - 1, y + 1};
        
        for (int xs : xCoords)
        {
            for (int ys : yCoords)
            {
                if ((xs >= 0 && xs < xSize && ys >= 0 && ys < ySize) && nodeList[xs][ys].getState())
                {
                    if(xs != x || ys != y)
                    {
                        result++;
                    }
                }
            }
        }
        
        return result;
    }
    
    public void changeNode(int x, int y)
    {
        nodeList[x][y].changeNextTurn();
        nodeList[x][y].change();
    }
    
    public void playTurn()
    {
        for (int i = 0; i < this.ySize; i++)
        {
            for (int j = 0; j < this.xSize; j++)
            {
                int neighbourCount = this.getNeighbourCount(j, i);
                if(nodeList[j][i].getState())
                {
                    // For currently live cells....
                    if(neighbourCount < 2)
                    {
                        nodeList[j][i].changeNextTurn();
                    }else if(neighbourCount > 3)
                    {
                        nodeList[j][i].changeNextTurn();
                    }else
                    {
                        // Do nothing... Just here for clarity...
                    }
                }else
                {
                    // for currently dead cells....
                    if(neighbourCount == 3)
                    {
                        nodeList[j][i].changeNextTurn();
                    }
                }
            }
        }
    }
    
    public void changeAll()
    {
        for (int i = 0; i < this.ySize; i++)
        {
            for (int j = 0; j < this.xSize; j++)
            {
                nodeList[j][i].change();
            }
        }
    }
    
    public void drawBoard(Graphics2D g2d)
    {
        for (int i = 0; i < this.ySize; i++)
        {
            for (int j = 0; j < this.xSize; j++)
            {
                if (nodeList[j][i].getState())
                {
                    g2d.fillRect(j * 10, i * 10, 10, 10);
                }
            }
        }
    }
}
