package com.ky.logic.utils.page;

import com.ky.logic.utils.page.restriction.AbstractCriterionCreater;
import com.ky.logic.utils.page.restriction.RelationalTable;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.CollectionPropertyNames;
import org.hibernate.sql.JoinType;

import java.util.ArrayList;
import java.util.List;

public class DBCriteriaBuilder {
    private String orderBy;
    private Criteria cr;
    private boolean orderAscending;
    private boolean resultTransformer;
    private List<Parameter> filterParams = new ArrayList<Parameter>();
    private List<AbstractCriterionCreater> criterionCreaters = new ArrayList<AbstractCriterionCreater>();
    private List<RelationalTable> relationalTables = new ArrayList<RelationalTable>();

    public Criteria getCr() {
        return cr;
    }

    public void setCr(Criteria cr) {
        this.cr = cr;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean getOrderAscending() {
        return orderAscending;
    }

    public void setOrderAscending(boolean orderAscending) {
        this.orderAscending = orderAscending;
    }

    public List<Parameter> getFilterParams() {
        return filterParams;
    }

    public void build() {

        addResultTransformerToCriteria();

        addRestrictionToCriteria();

        createRelations();

        createCriterionIntoCriteria();

        addOrderToCriteria();
    }


    private void addResultTransformerToCriteria() {
        if (resultTransformer == true) {
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        }
    }
    private void addRestrictionToCriteria() {
        if (filterParams.isEmpty()) {
            return;
        }

        for (Parameter parameter : filterParams) {

            if (parameter.isOrType()) {
                if (parameter.is2OrType()) {
                    List<Parameter> parameterList1 = parameter.getParameterList();
                    List<Parameter> parameterList2 = parameter.getParameterList2();
                    List<Criterion> criterionList1 = new ArrayList<Criterion>();
                    List<Criterion> criterionList2 = new ArrayList<Criterion>();
                    for (Parameter parameter1 : parameterList1) {
                        if (parameter1.isValue2Null()) {
                            if (parameter1.getValue() instanceof List) {
                                criterionList1.add(Restrictions.in(parameter1.getName(), ((List) parameter1.getValue()).toArray()));
                            } else if (null != parameter1.getDtdbType()) {
                                if (parameter1.getDtdbType() == DTDBType.ANYWHERE) {
                                    criterionList1.add(Restrictions.like(parameter1.getName(), parameter1.getValue().toString(), MatchMode.ANYWHERE));
                                } else if (parameter1.getDtdbType() == DTDBType.GREATER) {
                                    criterionList1.add(Restrictions.gt(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EQUAL_GREATER) {
                                    criterionList1.add(Restrictions.ge(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.LESS) {
                                    criterionList1.add(Restrictions.lt(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EQUAL_LESS) {
                                    criterionList1.add(Restrictions.le(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EXACT) {
                                    criterionList1.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.NE) {
                                    criterionList1.add(Restrictions.ne(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.NEORISNOTNULL) {
                                    criterionList1.add(Restrictions.neOrIsNotNull(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.COLLECTION_ELEMENT) {
                                    cr.createAlias(parameter1.getName(), "otherTable", JoinType.LEFT_OUTER_JOIN);
                                    criterionList1.add(Restrictions.like("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter1.getValue().toString(), MatchMode.ANYWHERE));
                                } else if (parameter1.getDtdbType() == DTDBType.COLLECTION_ELEMENT_EQUAL) {
                                    cr.createAlias(parameter1.getName(), "otherTable", JoinType.LEFT_OUTER_JOIN);
                                    criterionList1.add(Restrictions.eq("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter1.getValue().toString()));
                                }

                            } else {
                                criterionList1.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                            }
                        } else {
                            criterionList1.add(Restrictions.between(parameter1.getName(), parameter1.getValue(), parameter1.getValue2()));
                        }
                    }

                    Criterion criterion_1[] = new Criterion[criterionList1.size()];
                    int index = 0;
                    for (Criterion criteri : criterionList1) {
                        criterion_1[index] = criterionList1.get(index);
                        index++;
                    }
                    Criterion criterion1 = Restrictions.and(criterion_1);

                    for (Parameter parameter1 : parameterList2) {
                        if (parameter1.isValue2Null()) {
                            if (parameter1.getValue() instanceof List) {
                                criterionList2.add(Restrictions.in(parameter1.getName(), ((List) parameter1.getValue()).toArray()));
                            } else if (null != parameter1.getDtdbType()) {
                                if (parameter1.getDtdbType() == DTDBType.ANYWHERE) {
                                    criterionList2.add(Restrictions.like(parameter1.getName(), parameter1.getValue().toString(), MatchMode.ANYWHERE));
                                } else if (parameter1.getDtdbType() == DTDBType.GREATER) {
                                    criterionList2.add(Restrictions.gt(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EQUAL_GREATER) {
                                    criterionList2.add(Restrictions.ge(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.LESS) {
                                    criterionList2.add(Restrictions.lt(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EQUAL_LESS) {
                                    criterionList2.add(Restrictions.le(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EXACT) {
                                    criterionList2.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.NE) {
                                    criterionList2.add(Restrictions.ne(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.NEORISNOTNULL) {
                                    criterionList2.add(Restrictions.neOrIsNotNull(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.COLLECTION_ELEMENT) {
                                    cr.createAlias(parameter1.getName(), "otherTable", JoinType.LEFT_OUTER_JOIN);
                                    criterionList2.add(Restrictions.like("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter1.getValue().toString(), MatchMode.ANYWHERE));
                                } else if (parameter1.getDtdbType() == DTDBType.COLLECTION_ELEMENT_EQUAL) {
                                    cr.createAlias(parameter1.getName(), "otherTable", JoinType.LEFT_OUTER_JOIN);
                                    criterionList2.add(Restrictions.eq("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter1.getValue()));
                                }

                            } else {
                                criterionList2.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                            }
                        } else {
                            criterionList2.add(Restrictions.between(parameter1.getName(), parameter1.getValue(), parameter1.getValue2()));
                        }
                    }

                    Criterion criterion_2[] = new Criterion[criterionList2.size()];
                    index = 0;
                    for (Criterion criteri : criterionList2) {
                        criterion_2[index] = criterionList2.get(index);
                        index++;
                    }

                    Criterion criterion2 = Restrictions.and(criterion_2);

                    cr.add(Restrictions.or(criterion1, criterion2));

                } else {
                    List<Criterion> criterionList = new ArrayList<Criterion>();
                    List<Parameter> parameterList = parameter.getParameterList();
                    for (Parameter parameter1 : parameterList) {
                        if (parameter1.isValue2Null()) {
                            if (parameter1.getValue() instanceof List) {
                                criterionList.add(Restrictions.in(parameter1.getName(), ((List) parameter1.getValue()).toArray()));
                            }  else if (null != parameter1.getDtdbType()) {
                                if (parameter1.getDtdbType() == DTDBType.ANYWHERE) {
                                    criterionList.add(Restrictions.like(parameter1.getName(), parameter1.getValue().toString(), MatchMode.ANYWHERE));
                                } else if (parameter1.getDtdbType() == DTDBType.GREATER) {
                                    criterionList.add(Restrictions.gt(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EQUAL_GREATER) {
                                    criterionList.add(Restrictions.ge(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.LESS) {
                                    criterionList.add(Restrictions.lt(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EQUAL_LESS) {
                                    criterionList.add(Restrictions.le(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EXACT) {
                                    criterionList.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.EQUAL_NULL) {
                                    criterionList.add(Restrictions.eqOrIsNull(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.NE) {
                                    criterionList.add(Restrictions.ne(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.NEORISNOTNULL) {
                                    criterionList.add(Restrictions.neOrIsNotNull(parameter1.getName(), parameter1.getValue()));
                                } else if (parameter1.getDtdbType() == DTDBType.COLLECTION_ELEMENT) {
                                    cr.createAlias(parameter1.getName(), "otherTable", JoinType.LEFT_OUTER_JOIN);
                                    criterionList.add(Restrictions.like("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter1.getValue().toString(), MatchMode.ANYWHERE));
                                } else if (parameter1.getDtdbType() == DTDBType.COLLECTION_ELEMENT_EQUAL) {
                                    cr.createAlias(parameter1.getName(), "otherTable", JoinType.LEFT_OUTER_JOIN);
                                    criterionList.add(Restrictions.eq("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter1.getValue()));
                                }
                            } else {
                                criterionList.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                            }
                        } else {
                            criterionList.add(Restrictions.between(parameter1.getName(), parameter1.getValue(), parameter1.getValue2()));
                        }
                    }
                    Criterion criterion[] = new Criterion[criterionList.size()];
                    int index = 0;
                    for (Criterion criterion1 : criterionList) {
                        criterion[index] = criterionList.get(index);
                        index++;
                    }

                    cr.add(Restrictions.or(criterion));
                }
            }

            if (!parameter.isNull()) {
                if (parameter.isValue2Null()) {
                    if (parameter.getValue() instanceof List) {
                        cr.add(Restrictions.in(parameter.getName(), ((List) parameter.getValue()).toArray()));
                    } else if (null != parameter.getDtdbType()) {
                        if (parameter.getDtdbType() == DTDBType.ANYWHERE) {
                            cr.add(Restrictions.like(parameter.getName(), parameter.getValue().toString(), MatchMode.ANYWHERE));
                        } else if (parameter.getDtdbType() == DTDBType.GREATER) {
                            cr.add(Restrictions.gt(parameter.getName(), parameter.getValue()));
                        } else if (parameter.getDtdbType() == DTDBType.EQUAL_GREATER) {
                            cr.add(Restrictions.ge(parameter.getName(), parameter.getValue()));
                        } else if (parameter.getDtdbType() == DTDBType.LESS) {
                            cr.add(Restrictions.lt(parameter.getName(), parameter.getValue()));
                        } else if (parameter.getDtdbType() == DTDBType.EQUAL_LESS) {
                            cr.add(Restrictions.le(parameter.getName(), parameter.getValue()));
                        } else if (parameter.getDtdbType() == DTDBType.EXACT) {
                            cr.add(Restrictions.eq(parameter.getName(), parameter.getValue()));
                        } else if (parameter.getDtdbType() == DTDBType.EQUAL_NULL) {
                            cr.add(Restrictions.eqOrIsNull(parameter.getName(), parameter.getValue()));
                        } else if (parameter.getDtdbType() == DTDBType.NE) {
                            cr.add(Restrictions.ne(parameter.getName(), parameter.getValue()));
                        } else if (parameter.getDtdbType() == DTDBType.NEORISNOTNULL) {
                            cr.add(Restrictions.neOrIsNotNull(parameter.getName(), parameter.getValue()));
                        }
                    } else {
                        cr.add(Restrictions.eq(parameter.getName(), parameter.getValue()));
                    }
                } else {
                    if(parameter.getDtdbType() == DTDBType.RELATION_TABLE){
                        cr.createAlias(parameter.getName(), "relationalTable");
                        cr.add(Restrictions.like("relationalTable." + parameter.getValue().toString(), parameter.getValue2().toString(), MatchMode.ANYWHERE));
                    }else {
                        cr.add(Restrictions.between(parameter.getName(), parameter.getValue(), parameter.getValue2()));
                    }
                }
            }else{
                if(parameter.getDtdbType() == DTDBType.EQUAL_NULL){
                    cr.add(Restrictions.isNull(parameter.getName()));
                }else if(parameter.getDtdbType() == DTDBType.NEORISNOTNULL){
                    cr.add(Restrictions.isNotNull(parameter.getName()));
                }
            }
        }
    }

    private void createCriterionIntoCriteria() {
        for (AbstractCriterionCreater criterionCreater : criterionCreaters) {
            if (null == criterionCreater) {
                continue;
            }

            Criterion criterion = criterionCreater.createCriterion();
            if (null != criterion) {
                cr.add(criterion);
            }
        }
    }

    private void createRelations() {
        for (RelationalTable relationalTable : relationalTables) {
            if (null == relationalTable) {
                continue;
            }

            cr.createAlias(relationalTable.getAssociationPath(), relationalTable.getAliasName(), JoinType.LEFT_OUTER_JOIN);
        }
    }

    private boolean isOrderAscending() {
        return orderAscending;
    }

    public boolean isResultTransformer() {
        return resultTransformer;
    }

    private void addOrderToCriteria() {
        if (null == orderBy) {
            return;
        }

        if (isOrderAscending()) {
            cr.addOrder(Order.asc(orderBy));
        } else {
            cr.addOrder(Order.desc(orderBy));
        }
    }

    public void setResultTransformer(boolean resultTransformer) {
        this.resultTransformer = resultTransformer;
    }

    public void addCriterionCreater(AbstractCriterionCreater criterionCreater) {
        criterionCreaters.add(criterionCreater);
    }

    public void addRelationalTable(RelationalTable relationalTable) {
        relationalTables.add(relationalTable);
    }
}
