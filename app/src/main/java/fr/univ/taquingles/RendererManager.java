package fr.univ.taquingles;

import android.opengl.GLES30;
import android.util.Pair;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univ.taquingles.taquin.Forme;

/**
 * Classe Singleton pour gérer tous les modèles à représenter.
 */
public class RendererManager {

    private final String vertexShaderCode =
            "#version 300 es\n"+
            "uniform mat4 uMVPMatrix;\n"+
            "in vec3 vPosition;\n" +
            "in vec4 vCouleur;\n"+
            "out vec4 Couleur;\n"+
            "void main() {\n" +
            "gl_Position = uMVPMatrix * vec4(vPosition,1.0);\n" +
            "Couleur = vCouleur;\n"+
            "}\n";

    private final String fragmentShaderCode =
            "#version 300 es\n"+
            "precision mediump float;\n" + // pour définir la taille d'un float
            "in vec4 Couleur;\n"+
            "out vec4 fragColor;\n"+
            "void main() {\n" +
            "fragColor = Couleur;\n" +
            "}\n";

    private int[] linkStatus = {0};
    private int idProgram;
    private int idVertexShader;
    private int idFragmentShader;

    private static RendererManager sInstance;
    private Map<Forme, AForme> formes;
    private Map<Forme, IntBuffer> formesIndices;
    private float[] verticesArray;

    // VBOS
    private int idVertices;
    private int idColors;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;

    private int IdMVPMatrix;

    static final int COORDS_PER_VERTEX = 3; // nombre de coordonnées par vertex
    static final int COULEURS_PER_VERTEX = 4; // nombre de composantes couleur par vertex

    private final int vertexStride = COORDS_PER_VERTEX * 4; // le pas entre 2 sommets : 4 bytes per vertex

    private final int couleurStride = COULEURS_PER_VERTEX * 4; // le pas entre 2 couleurs

    private List<Pair<Forme, FormeParam>> drawQueue;

    public static RendererManager getInstance(){
        if(sInstance == null){
            sInstance = new RendererManager();
        }

        return sInstance;
    }

    public RendererManager(){
        this.formes = new HashMap<>();
        this.formesIndices = new HashMap<>();
    }

    public void nouvelleForme(Forme forme, AForme donnees){
        this.formes.put(forme, donnees);
    }

    public void init(){
        // Parcours de toutes les formes disponibles
        List<Float> verticesList = new ArrayList<>();

        // A REFAIRE @ TODO
        for(Map.Entry<Forme, AForme> entry : this.formes.entrySet()){
            float[] vertices = entry.getValue().getVertices();
            int[] indices = entry.getValue().getIndices();

            ByteBuffer bb = ByteBuffer.allocateDirect(indices.length * 4);
            bb.order(ByteOrder.nativeOrder());
            IntBuffer ib = bb.asIntBuffer();
            indices = Arrays.stream(indices).map(e -> e + (verticesList.size() / COORDS_PER_VERTEX)).toArray();
            ib.put(indices);
            ib.flip();

            this.formesIndices.put(entry.getKey(), ib);

            // Ajout dans le vertices buffer
            for(int i = 0; i < vertices.length; i++){
                verticesList.add(vertices[i]);
            }
        }

        this.verticesArray = new float[verticesList.size()];

        // On rempli le tableau de vertices
        for(int i = 0; i < verticesList.size(); i++){
            this.verticesArray[i] = verticesList.get(i);
        }

        // initialisation du buffer pour les vertex (4 bytes par float)
        ByteBuffer bb = ByteBuffer.allocateDirect(this.verticesArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.verticesBuffer = bb.asFloatBuffer();
        this.verticesBuffer.put(verticesArray);
        this.verticesBuffer.flip();

        /* Chargement des shaders */
        this.idVertexShader = MyGLRenderer.loadShader(
                GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        this.idFragmentShader = MyGLRenderer.loadShader(
                GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        this.idProgram = GLES30.glCreateProgram();           // create empty OpenGL Program
        GLES30.glAttachShader(idProgram, this.idVertexShader);     // add the vertex shader to program
        GLES30.glAttachShader(idProgram, this.idFragmentShader);   // add the fragment shader to program
        GLES30.glLinkProgram(idProgram);                    // create OpenGL program executables
        GLES30.glGetProgramiv(idProgram, GLES30.GL_LINK_STATUS, linkStatus,0);
    }

    public void draw(Pair<Forme, FormeParam> p, float[] mvpMatrix){
        // Add program to OpenGL environment
        GLES30.glUseProgram(this.idProgram);

        // get handle to shape's transformation matrix
        this.IdMVPMatrix = GLES30.glGetUniformLocation(this.idProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES30.glUniformMatrix4fv(this.IdMVPMatrix, 1, false, mvpMatrix, 0);

        // get handle to vertex shader's vPosition member et vCouleur member
        this.idVertices = GLES30.glGetAttribLocation(this.idProgram, "vPosition");
        this.idColors = GLES30.glGetAttribLocation(this.idProgram, "vCouleur");

        /* Activation des Buffers */
        GLES30.glEnableVertexAttribArray(this.idVertices);
        GLES30.glEnableVertexAttribArray(this.idColors);

        /* Lecture des Buffers */
        GLES30.glVertexAttribPointer(
                this.idVertices, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                this.vertexStride, this.verticesBuffer);

        int cap = this.verticesArray.length;

        GLES30.glVertexAttribPointer(
                this.idColors, COULEURS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                this.couleurStride, p.second.getCouleur().getCouleurBuffer(cap));


        IntBuffer ib = this.formesIndices.get(p.first);

        // Draw the square
        GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, ib.capacity(),
                GLES30.GL_UNSIGNED_INT, ib);

        // Disable vertex array
        GLES30.glDisableVertexAttribArray(this.idVertices);
        GLES30.glDisableVertexAttribArray(this.idColors);
    }
}
