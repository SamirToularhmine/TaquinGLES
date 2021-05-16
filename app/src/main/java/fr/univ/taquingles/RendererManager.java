package fr.univ.taquingles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Pair;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univ.taquingles.formes.AForme;
import fr.univ.taquingles.formes.FormeParam;
import fr.univ.taquingles.taquin.Couleur;
import fr.univ.taquingles.taquin.Forme;

/**
 * Classe Singleton pour gérer tous les modèles à représenter.
 */
public class RendererManager {

    private static final String vertexShaderCode =
            "#version 300 es\n"+
            "uniform mat4 uMVPMatrix;\n"+
            "in vec3 vPosition;\n" +
            "in vec4 vCouleur;\n"+
            "out vec4 Couleur;\n"+
            "in vec2 texCoords;\n"+
            "out vec2 passTexCoords;\n"+
            "void main() {\n" +
            "gl_Position = uMVPMatrix * vec4(vPosition,1.0);\n" +
            "Couleur = vCouleur;\n"+
            "passTexCoords = texCoords;\n"+
            "}\n";

    private static final String fragmentShaderCode =
            "#version 300 es\n"+
            "precision mediump float;\n" + // pour définir la taille d'un float
            "in vec4 Couleur;\n"+
            "out vec4 fragColor;\n"+
            "in vec2 passTexCoords;\n"+
            "uniform sampler2D textureSampler;\n"+
            "uniform int hasTexture;\n"+
            "void main() {\n" +
            "if(hasTexture == 0){\n"+
            "fragColor = Couleur;\n" +
            "}else{\n"+
            "vec2 flipped_texcoords = vec2(passTexCoords.x, 1.0 - passTexCoords.y);\n" +
            "fragColor = texture(textureSampler, flipped_texcoords);\n" +
            "}\n"+
            "}\n";

    private static final float[] quadTexCoords = new float[]{
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,

    } ;

    private final int[] linkStatus = {0};
    private final int[] textureHandle = new int[1];

    private int idProgram;

    private static RendererManager sInstance;

    private final Map<Forme, AForme> formes;
    private final Map<Forme, IntBuffer> formesIndices;
    private final Map<Forme, List<Buffer>> formesVao;

    private static final int COORDS_PER_VERTEX = 3; // nombre de coordonnées par vertex
    private static final int COULEURS_PER_VERTEX = 4; // nombre de composantes couleur par vertex
    private static final int TEXCOORD_PER_VERTEX = 2; // nombre de composantes couleur par vertex

    private static final int vertexStride = COORDS_PER_VERTEX * 4; // le pas entre 2 sommets : 4 bytes per vertex
    private static final int couleurStride = COULEURS_PER_VERTEX * 4; // le pas entre 2 couleurs
    private static final int texCoordsStride = TEXCOORD_PER_VERTEX * 4; // le pas entre 2 texCoords


    public static RendererManager getInstance(){
        if(sInstance == null){
            sInstance = new RendererManager();
        }

        return sInstance;
    }

    public RendererManager(){
        this.formes = new HashMap<>();
        this.formesIndices = new HashMap<>();
        this.formesVao = new HashMap<>();
    }

    public void nouvelleForme(Forme forme, AForme donnees){
        this.formes.put(forme, donnees);
        this.formesVao.put(forme, new ArrayList<>());
    }

    public void init(){
        // Parcours de toutes les formes disponibles
        for(Map.Entry<Forme, AForme> entry : this.formes.entrySet()){
            float[] vertices = entry.getValue().getVertices();
            int[] indices = entry.getValue().getIndices();

            ByteBuffer bb = ByteBuffer.allocateDirect(indices.length * 4);
            bb.order(ByteOrder.nativeOrder());
            IntBuffer ib = bb.asIntBuffer();
            ib.put(indices);
            ib.flip();

            this.formesIndices.put(entry.getKey(), ib);

            // initialisation du buffer pour les vertex (4 bytes par float)
            bb = ByteBuffer.allocateDirect(vertices.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer vb = bb.asFloatBuffer();
            vb.put(vertices);
            vb.flip();

            this.formesVao.get(entry.getKey()).add(vb);

            // initialisation du buffer pour les textcoords
            ByteBuffer bb1 = ByteBuffer.allocateDirect(quadTexCoords.length * 4);
            bb1.order(ByteOrder.nativeOrder());
            FloatBuffer tcb = bb1.asFloatBuffer();
            tcb.put(quadTexCoords);
            tcb.flip();

            this.formesVao.get(entry.getKey()).add(tcb);
        }

        /* Chargement des shaders */
        int idVertexShader = MyGLRenderer.loadShader(
                GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        int idFragmentShader = MyGLRenderer.loadShader(
                GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        this.idProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(idProgram, idVertexShader);
        GLES30.glAttachShader(idProgram, idFragmentShader);
        GLES30.glLinkProgram(idProgram);
        GLES30.glGetProgramiv(idProgram, GLES30.GL_LINK_STATUS, linkStatus,0);
    }

    public void addTexture(Context context, int resourceId){
        this.textureHandle[0] = Utils.loadTexture(context, resourceId);
    }

    public void draw(Pair<Forme, FormeParam> p, float[] mvpMatrix){
        GLES30.glUseProgram(this.idProgram);

        int idMVPMatrix = GLES30.glGetUniformLocation(this.idProgram, "uMVPMatrix");

        GLES30.glUniformMatrix4fv(idMVPMatrix, 1, false, mvpMatrix, 0);

        int idVertices = GLES30.glGetAttribLocation(this.idProgram, "vPosition");
        int idColors = GLES30.glGetAttribLocation(this.idProgram, "vCouleur");
        int idTexCoords = GLES30.glGetAttribLocation(this.idProgram, "texCoords");

        int mTextureUniformHandle = GLES30.glGetUniformLocation(this.idProgram, "textureSampler");
        int hasTextureUniformHandle = GLES30.glGetUniformLocation(this.idProgram, "hasTexture");

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, this.textureHandle[0]);

        GLES30.glUniform1i(mTextureUniformHandle, 0);

        if(p.second.isTextured()){
            GLES30.glUniform1i(hasTextureUniformHandle, 1);
        }else{
            GLES30.glUniform1i(hasTextureUniformHandle, 0);
        }

        GLES30.glEnableVertexAttribArray(idVertices);
        GLES30.glEnableVertexAttribArray(idColors);
        GLES30.glEnableVertexAttribArray(idTexCoords);

        GLES30.glVertexAttribPointer(
                idVertices, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                vertexStride, this.formesVao.get(p.first).get(0));

        int cap = this.formesVao.get(p.first).get(0).capacity();

        GLES30.glVertexAttribPointer(
                idColors, COULEURS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                couleurStride, p.second.getCouleur().getCouleurBuffer(cap));

        GLES30.glVertexAttribPointer(
                idTexCoords, 2,
                GLES30.GL_FLOAT, false,
                texCoordsStride, this.formesVao.get(p.first).get(1));

        IntBuffer ib = this.formesIndices.get(p.first);

        GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, ib.capacity(),
                GLES30.GL_UNSIGNED_INT, ib);

        GLES30.glDisableVertexAttribArray(idVertices);
        GLES30.glDisableVertexAttribArray(idColors);
        GLES30.glDisableVertexAttribArray(idTexCoords);
    }
}
