/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.annotation.Column;
import com.dostojic.njt.annotation.Table;
import com.dostojic.njt.db.Database;
import com.dostojic.njt.util.WrappedException;
import com.dostojic.njt.db.util.CommonUtils;
import com.dostojic.njt.db.util.QueryUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.lang.model.type.TypeVariable;

/**
 *
 * @author dejan
 */
public abstract class GenericDao<Dto> {

    public String getCommaSepColumnList() {
        return commaSepColumnList;
    }

    protected void setCommaSepColumnList(String commaSepColumnList) {
        this.commaSepColumnList = commaSepColumnList;
    }

    public interface Col {

        public String getColumnName();

        public void resultSetToDto(ResultSet rs, TypeVariable dto, int index) throws SQLException;

        public void dtoToParam(TypeVariable dto, PreparedStatement statement, int paramIndex) throws SQLException;

        public boolean isPrimaryKey();
    }
    
    public abstract class ColumnMapper {

        String columnName;

        public ColumnMapper(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }

        public abstract void resultSetToDto(ResultSet rs, Dto dto, int index) throws SQLException;

        public abstract void dtoToParam(Dto dto, PreparedStatement statement, int paramIndex) throws SQLException;

        public boolean isPrimaryKey(){
            return false;
        }
    }

    protected abstract Dto newDto();
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    protected void setTableName(String tableName) {
        this.tableName = tableName;
    }
    private boolean autoIncrement;

    protected boolean isAutoIncrement() {
        return autoIncrement;
    }

    protected void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    protected void setPrimaryKey(Dto dto, long id) {
        throw new UnsupportedOperationException();
    }

    private List<ColumnMapper> fields = new ArrayList<>();

    protected void add(ColumnMapper f) {
        fields.add(f);
    }

    public List<ColumnMapper> getFields() {
        return fields;
    }

    private String commaSepColumnList;
    private String cloumnParamList;
    private int columnCount;
    private String pkClosure;
    private int pkCount;
    private String nonPkSetters;
    private int nonPkCount;
    private List<ColumnMapper> pkFields;
    private List<ColumnMapper> nonPkFields;

    public String getPkClosure() {
        return pkClosure;
    }

    public int getPkCount() {
        return pkCount;
    }

    public List<ColumnMapper> getPkFields() {
        return pkFields;
    }

    private enum Type {

        getter, setter, other
    }

    private static AbstractMap.SimpleEntry<String, Type> processName(Method method) {

        // preconditions
        assert !CommonUtils.isEmpty(method) && !CommonUtils.isEmpty(method.getName()) : "Prosledjen je prazan method!";

        String methodName = method.getName();
        int paramCount = method.getParameterTypes().length;

        AbstractMap.SimpleEntry<String, Type> ret = new AbstractMap.SimpleEntry(null, Type.other);

        Type t = Type.other;
        String propertyName = null;
        if (methodName.startsWith("get") && paramCount == 0) {
            propertyName = methodName.substring(3);
            t = Type.getter;
        }
        if (methodName.startsWith("is") && paramCount == 0) {
            propertyName = methodName.substring(2);
            t = Type.getter;
        }
        if (methodName.startsWith("set") && paramCount == 1) {
            propertyName = methodName.substring(3);
            t = Type.setter;
        }

        if (!CommonUtils.isEmpty(propertyName)) {
            char c = propertyName.charAt(0);

            if (Character.isUpperCase(c)) {
                propertyName = Character.toLowerCase(c) + propertyName.substring(1);
                ret = new AbstractMap.SimpleEntry(propertyName, t);
            }
        }

        // postconditions
        // ili nije ni geter ni seter i vracam (null, other)
        // ili je nesto od ta dva i vracam tip i neprazno ime propertija
        assert (ret.getValue().equals(Type.other) == CommonUtils.isEmpty(ret.getKey()));
        return ret;
    }

    public void annotationConfig() {
        Class dtoType = newDto().getClass();
        if (dtoType.isAnnotationPresent(Table.class)) {

            Table table = (Table) dtoType.getAnnotation(Table.class);
            setTableName(table.name());
            setAutoIncrement(table.autoIncrement());
            

            Method[] methods = dtoType.getMethods();
            Map<String, Method> getters = new HashMap<>();
            Map<String, Method> setters = new HashMap<>();

            for (Method method : methods) {
                AbstractMap.SimpleEntry<String, Type> processedName = processName(method);
                String propertyName = (String) processedName.getKey();
                Type t = processedName.getValue();

                if (t.equals(Type.getter)) {
                    getters.put(propertyName, method);
                } else if (t.equals(Type.setter)) {
                    setters.put(propertyName, method);
                }
            }

            Stream.of(dtoType.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .forEach(field -> {
                        Column column = field.getAnnotation(Column.class);
                        final String fieldName = field.getName(); // name of field
                        String columnName = column.name(); // name of col in db
                        final boolean primaryKey = column.isPrimaryKey();
                        final Class<?> type = field.getType();
                        final Method getter = getters.get(fieldName);
                        final Method setter = setters.get(fieldName);
                
                        add(new ColumnMapper(columnName) {

                            @Override
                            public boolean isPrimaryKey() {
                                return primaryKey;
                            }

                            @Override
                            public void resultSetToDto(ResultSet rs, Dto dto, int index) throws SQLException {
                                if (setter == null) {
                                    throw new UnsupportedOperationException("field " + fieldName + " has no declared public setter method and can't be read from database!");
                                }
                                try {
                                    Class a = ResultSet.class;
                                    rs.getString(index);
                                    if (setter.getParameterTypes()[0].isAssignableFrom(type)) {
                                        setter.invoke(dto, rs.getObject(index, type));
                                    }
                                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                    Logger.getLogger(ArtistDao.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            @Override
                            public void dtoToParam(Dto dto, PreparedStatement statement, int paramIndex) throws SQLException {
                                if (getter == null) {
                                    throw new UnsupportedOperationException("field " + fieldName + " has no declared public getter method and can't be persisted to database!");
                                }

                                try {
                                    statement.setObject(paramIndex, getter.invoke(dto));
                                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                    Logger.getLogger(ArtistDao.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        });

                    });

        }else{
            config();
        }
    }

    public GenericDao() {
        annotationConfig();
        init();
    }
    
    protected abstract void config();

    protected void init() {
        StringBuilder columnListBuilder = new StringBuilder();
        StringBuilder columnParamBuilder = new StringBuilder();
        StringBuilder pkListBuilder = new StringBuilder();
        StringBuilder nonPkListBuilder = new StringBuilder();
        StringBuilder nonPkSettersBuilder = new StringBuilder();
        StringBuilder pkClosureBuilder = new StringBuilder();

        pkFields = new ArrayList<>();
        nonPkFields = new ArrayList<>();

        columnCount = 0;
        pkCount = 0;
        nonPkCount = 0;
        for (ColumnMapper f : fields) {
            if (++columnCount != 1) {
                columnListBuilder.append(",");
                columnParamBuilder.append(",");
            }
            columnListBuilder.append(getTableName()).append(".").append(f.getColumnName());
            columnParamBuilder.append("?");
            if (f.isPrimaryKey()) {
                if (++pkCount != 1) {
                    pkListBuilder.append(",");
                    pkClosureBuilder.append(" and ");
                }
                pkListBuilder.append(f.getColumnName());
                pkClosureBuilder.append(getTableName()).append(".").append(f.getColumnName()).append("=?");
                pkFields.add(f);
            } else {
                if (++nonPkCount != 1) {
                    nonPkListBuilder.append(",");
                    nonPkSettersBuilder.append(",");
                }
                nonPkListBuilder.append(f.getColumnName());
                nonPkSettersBuilder.append(f.getColumnName()).append("=?");
                nonPkFields.add(f);
            }
        }
        setCommaSepColumnList(columnListBuilder.toString());
        nonPkSetters = nonPkSettersBuilder.toString();
        pkClosure = pkClosureBuilder.toString();
        cloumnParamList = columnParamBuilder.toString();
    }

    public void loadFromResultSet(ResultSet rs, Dto dto) {
        try {
            for (int i = 0; i < fields.size(); i++) {
                fields.get(i).resultSetToDto(rs, dto, i + 1);
            }
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    public List<Dto> loadList(String whereCondition, String orderBy, int offset, int count) {
        final List<Dto> list = new ArrayList<>();
        String sql = makeSql(whereCondition, orderBy, offset, count);

        QueryUtils.forEach(sql, (ResultSet rs) -> {
            Dto dto = newDto();
            loadFromResultSet(rs, dto);
            list.add(dto);
        });
        return list;
    }

    public <T extends Dto> List<T> loadList(String whereCondition, String orderBy, int offset, int count, final Class<T> c) {
        final List<T> list = new ArrayList<>();
        String sql = makeSql(whereCondition, orderBy, offset, count);

        QueryUtils.forEach(sql, (ResultSet rs) -> {
            try {
                T dto = c.newInstance();
                loadFromResultSet(rs, dto);
                list.add(dto);
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        return list;
    }

    public List<Dto> loadList(String whereCondition, String orderBy) {
        return loadList(whereCondition, orderBy, 0, 0);
    }

    public <T extends Dto> List<T> loadList(String whereCondition, String orderBy, final Class<T> c) {
        return loadList(whereCondition, orderBy, 0, 0, c);
    }

    public List<Dto> loadAll() {
        return loadList(null, null, 0, 0);
    }
    
    public <T extends Dto> List<T> laodAll(final Class<T> c) {
        return loadList(null, null, 0, 0, c);
    }

    public Dto loadFirst(String whereCondition) {
        List<? extends Dto> tmp = loadList(whereCondition, null, 0, 1);
        Dto ret = null;

        if (!tmp.isEmpty()) {
            ret = tmp.get(0);
        }

        return ret;
    }

    public Dto loadFirst(String whereCondition, String orderBy) {
        List<? extends Dto> tmp = loadList(whereCondition, orderBy, 0, 1);
        Dto ret = null;

        if (!tmp.isEmpty()) {
            ret = tmp.get(0);
        }

        return ret;
    }

    public <T extends Dto> T loadFirst(String whereCondition, String orderBy, Class<T> c) {
        List<T> tmp = loadList(whereCondition, orderBy, 0, 1, c);
        T ret = null;

        if (!tmp.isEmpty()) {
            ret = tmp.get(0);
        }

        return ret;
    }

    public Dto loadByPk(long id) {
        Dto dto = newDto();
        setPrimaryKey(dto, id);

        if (load(dto)) {
            return dto;
        } else {
            return null;
        }
    }

    public boolean load(Dto dto) {
        try {
            String sql
                    = "select " + getCommaSepColumnList() + " "
                    + "from " + getTableName() + " "
                    + "where " + pkClosure;
            PreparedStatement st = Database.getCurrent().getConnection().prepareStatement(sql);
            try {
                for (int i = 0; i < pkCount; i++) {
                    pkFields.get(i).dtoToParam(dto, st, i + 1);
                }
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    loadFromResultSet(rs, dto);
                    return true;
                } else {
                    return false;
                }
            } finally {
                st.close();
            }
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    public int count(String whereCondition) {
        try {
            int count = 0;
            String sql
                    = "select count(*) "
                    + "from " + getTableName() + " "
                    + (CommonUtils.isEmpty(whereCondition)
                    ? "" : "where " + whereCondition);
            Statement st = Database.getCurrent().createStatement();
            try {
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            } finally {
                st.close();
            }
            return count;
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    public int countAll() {
        return count(null);
    }

    public boolean delete(String whereCondition) {
        try {
            String sql
                    = "delete "
                    + "from " + getTableName() + " "
                    + (CommonUtils.isEmpty(whereCondition)
                    ? "" : "where " + whereCondition);

            try (Statement st = Database.getCurrent().createStatement()) {
                int n = st.executeUpdate(sql);
                return n > 0;
            }
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    public boolean delete(Dto dto) {
        try {
            String sql
                    = "delete from " + getTableName() + " "
                    + "where " + pkClosure;

            try (PreparedStatement st = Database.getCurrent().getConnection().prepareStatement(sql)) {
                for (int i = 0; i < pkCount; i++) {
                    pkFields.get(i).dtoToParam(dto, st, i + 1);
                }
                int n = st.executeUpdate();
                return n > 0;
            }
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    public boolean deleteByPk(long primaryKey) {
        Dto dto = newDto();
        setPrimaryKey(dto, primaryKey);

        return delete(dto);
    }

    public boolean update(Dto dto) {
        try {
            String updateSql
                    = "update " + getTableName() + " "
                    + "set " + nonPkSetters + " "
                    + "where " + pkClosure;
            try (PreparedStatement updateStatement = Database.getCurrent().getConnection().prepareStatement(updateSql)) {
                for (int i = 0; i < nonPkCount; i++) {
                    nonPkFields.get(i).dtoToParam(dto, updateStatement, i + 1);
                }
                for (int j = 0; j < pkCount; j++) {
                    pkFields.get(j).dtoToParam(dto, updateStatement, j + nonPkCount + 1);
                }
                int n = updateStatement.executeUpdate();
                return n != 0;
            }
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    public void insert(Dto dto) {
        try {
            Connection conn = Database.getCurrent().getConnection();
            String insertSql
                    = "insert into  " + getTableName() + " "
                    + "(" + getCommaSepColumnList() + ") "
                    + "values (" + cloumnParamList + ")";

            PreparedStatement insertStatement;
            if (isAutoIncrement()) {
                insertStatement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            } else {
                insertStatement = conn.prepareStatement(insertSql);
            }
            try {
                for (int i = 0; i < columnCount; i++) {
                    fields.get(i).dtoToParam(dto, insertStatement, i + 1);
                }
                insertStatement.executeUpdate();
                if (isAutoIncrement()) {
                    ResultSet rs = insertStatement.getGeneratedKeys();
                    rs.next();
                    setPrimaryKey(dto, rs.getLong(1));
                }
            } finally {
                insertStatement.close();
            }
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    public void updateOrInsert(Dto dto) {
        if (!update(dto)) {
            insert(dto);
        }
    }

    protected String makeSql(String whereCondition, String orderBy, int offset, int count) {
        String sql
                = "select " + getCommaSepColumnList() + " "
                + "from " + getTableName() + " "
                + (CommonUtils.isEmpty(whereCondition)
                ? "" : "where " + whereCondition + " ")
                + (CommonUtils.isEmpty(orderBy)
                ? "" : "order by " + orderBy)
                + (count > 0 ? " limit " + offset + "," + count : "");
        
        return sql;
    }
    /*
     * Not finished! What if not all elements are not insered because of constrains?!
     * 
     public void insert(List<Dto> entities) throws SQLException {
     try {
     Connection conn = Database.getCurrent().getConnection();
     PreparedStatement insertStatement = null;
     String insertSql =
     "insert into  " + getTableName() + " "
     + "(" + getCommaSepColumnList() + ") "
     + "values (" + cloumnParamList + ")";
        
     if (isAutoIncrement()) {
     insertStatement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
     } else {
     insertStatement = conn.prepareStatement(insertSql);
     }
        
     for (int j = 0; j < entities.size(); j++) {
     try {
     for (int i = 0; i < columnCount; i++) {
     fields.get(i).dtoToParam(entities.get(j), insertStatement, i + 1);
     }
     insertStatement.addBatch();

     } finally {
     insertStatement.close();
     }

     }

     insertStatement.executeBatch();
     if (isAutoIncrement()) {
     ResultSet rs = insertStatement.getGeneratedKeys();
     for (int j = 0; j < entities.size(); j++) {
     rs.next();
     setPrimaryKey(entities.get(j), rs.getLong(1));
     }
     }
     } catch (SQLException e) {
     throw new WrappedException(e);
     }
     }
     */
}
